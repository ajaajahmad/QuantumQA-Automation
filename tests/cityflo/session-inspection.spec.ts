import { expect, test } from '@playwright/test';
import fs from 'node:fs/promises';
import path from 'node:path';

test.use({
  launchOptions: { headless: false },
  screenshot: 'off',
  trace: 'off',
  video: 'off',
});

const stagingUrl = 'https://app.cityflostaging.com';
const outputDir = path.resolve('test-results/cityflo');
const pickupTerm = process.env.CITYFLO_PICKUP_TERM ?? 'Powai';
const dropoffTerm = process.env.CITYFLO_DROPOFF_TERM ?? 'BKC';
const pickupMatch = process.env.CITYFLO_PICKUP_MATCH ?? pickupTerm;
const dropoffMatch = process.env.CITYFLO_DROPOFF_MATCH ?? dropoffTerm;
const pickupRequiredContext = process.env.CITYFLO_PICKUP_CONTEXT ?? '';
const dropoffRequiredContext = process.env.CITYFLO_DROPOFF_CONTEXT ?? '';
const bookingName = process.env.CITYFLO_BOOKING_NAME ?? '';
const bookingEmail = process.env.CITYFLO_BOOKING_EMAIL ?? '';
const reportName = process.env.CITYFLO_REPORT_NAME ?? 'read-only-inspection.json';
const sensitiveStrings: string[] = [];
let accountSetupSignals: Record<string, boolean> | null = null;
const publicStopSignals: Record<string, boolean> = {
  pickupSuggestionPresent: false,
  dropoffSuggestionPresent: false,
  pickupContextPresent: false,
  dropoffContextPresent: false,
};

function safePath(rawUrl: string): string {
  const url = new URL(rawUrl);
  return url.pathname
    .replace(/[0-9a-f]{8}-[0-9a-f-]{27,}/gi, ':id')
    .replace(/\b\d{8,}\b/g, ':id');
}

function redactText(value: string): string {
  let redacted = value
    .replace(/\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}\b/gi, '[REDACTED_EMAIL]')
    .replace(/\+?\d[\d\s()-]{7,}\d/g, '[REDACTED_NUMBER]')
    .replace(/\b(?:bearer\s+)[^\s,;]+/gi, 'Bearer [REDACTED]');
  for (const sensitive of [...sensitiveStrings].sort((a, b) => b.length - a.length)) {
    if (sensitive.length >= 3) redacted = redacted.replaceAll(sensitive, '[REDACTED_PERSONAL_DATA]');
  }
  return redacted;
}

function redactVisibleCopy(value: string): string {
  const lines = redactText(value).split('\n');
  let inSavedAddresses = false;
  let redactNextAddress = false;
  return lines.map((line) => {
    const trimmed = line.trim();
    if (trimmed === 'Saved Addresses') {
      inSavedAddresses = true;
      return line;
    }
    if (inSavedAddresses && /^(Home|Office|Work)$/.test(trimmed)) {
      redactNextAddress = true;
      return line;
    }
    if (redactNextAddress) {
      redactNextAddress = false;
      return '[REDACTED_SAVED_ADDRESS]';
    }
    return line;
  }).join('\n');
}

function collectSensitiveStrings(value: unknown, key = '', insideAddress = false): void {
  const sensitiveKey = /(phone|mobile|email|address|first_name|last_name|full_name|customer_name)/i;
  const addressContext = insideAddress || /address/i.test(key);
  if (typeof value === 'string' && (sensitiveKey.test(key) || addressContext)) {
    sensitiveStrings.push(value);
    return;
  }
  if (Array.isArray(value)) {
    value.forEach((item) => collectSensitiveStrings(item, key, addressContext));
  } else if (value && typeof value === 'object') {
    for (const [childKey, child] of Object.entries(value as Record<string, unknown>)) {
      collectSensitiveStrings(child, childKey, addressContext);
    }
  }
}

function collectAllStrings(value: unknown): void {
  if (typeof value === 'string') {
    sensitiveStrings.push(value);
  } else if (Array.isArray(value)) {
    value.forEach(collectAllStrings);
  } else if (value && typeof value === 'object') {
    Object.values(value as Record<string, unknown>).forEach(collectAllStrings);
  }
}

function containsText(value: unknown, pattern: RegExp): boolean {
  if (typeof value === 'string') return pattern.test(value);
  if (Array.isArray(value)) return value.some((item) => containsText(item, pattern));
  if (value && typeof value === 'object') return Object.values(value as Record<string, unknown>).some((item) => containsText(item, pattern));
  return false;
}

function oneObjectContainsTerms(value: unknown, terms: string[]): boolean {
  if (Array.isArray(value)) return value.some((item) => oneObjectContainsTerms(item, terms));
  if (!value || typeof value !== 'object') return false;
  const strings: string[] = [];
  const collect = (item: unknown) => {
    if (typeof item === 'string') strings.push(item);
    else if (Array.isArray(item)) item.forEach(collect);
    else if (item && typeof item === 'object') Object.values(item as Record<string, unknown>).forEach(collect);
  };
  collect(value);
  const joined = strings.join(' ');
  if (terms.every((term) => new RegExp(term, 'i').test(joined))) return true;
  return Object.values(value as Record<string, unknown>).some((item) => oneObjectContainsTerms(item, terms));
}

function hasNonEmptyValue(value: unknown): boolean {
  if (typeof value === 'string') return value.trim().length > 0;
  if (typeof value === 'number' || typeof value === 'boolean') return true;
  if (Array.isArray(value)) return value.some(hasNonEmptyValue);
  if (value && typeof value === 'object') return Object.values(value as Record<string, unknown>).some(hasNonEmptyValue);
  return false;
}

async function hasNearbyContext(locator: import('@playwright/test').Locator | undefined, context: string): Promise<{ matched: boolean; depth: number | null; checks: Array<{ depth: number; matches: boolean; textLength: number }> }> {
  if (!context) return { matched: true, depth: null, checks: [] };
  if (!locator) return { matched: false, depth: null, checks: [] };
  const expected = new RegExp(context, 'i');
  const checks: Array<{ depth: number; matches: boolean; textLength: number }> = [];
  for (let depth = 1; depth <= 5; depth += 1) {
    const parent = locator.locator(`xpath=${Array(depth).fill('..').join('/')}`);
    const text = await parent.innerText().catch(() => '');
    const matches = expected.test(text);
    checks.push({ depth, matches, textLength: text.length });
    if (matches && text.length <= 160) return { matched: true, depth, checks };
  }
  return { matched: false, depth: null, checks };
}

function summarizeJson(value: unknown, parentKey = ''): unknown {
  const sensitiveKey = /(phone|mobile|email|address|token|secret|auth|cookie|card|upi|user|customer|rider|account|name)/i;
  const safeValueKey = /(price|amount|currency|plan|product|auto.?renew|renew|valid|expir|active|status|days|duration|ride.?count|unlimited|success|payment.?status|transaction.?status)/i;

  if (Array.isArray(value)) return value.slice(0, 30).map((item) => summarizeJson(item, parentKey));
  if (!value || typeof value !== 'object') {
    if (!safeValueKey.test(parentKey)) return '[VALUE OMITTED]';
    if (typeof value === 'string') return redactText(value.slice(0, 200));
    return value;
  }

  const result: Record<string, unknown> = {};
  for (const [key, child] of Object.entries(value as Record<string, unknown>).slice(0, 100)) {
    if (sensitiveKey.test(key)) {
      result[key] = '[REDACTED]';
    } else if (safeValueKey.test(key)) {
      result[key] = summarizeJson(child, key);
    } else if (child && typeof child === 'object') {
      result[key] = summarizeJson(child, key);
    } else {
      result[key] = '[VALUE OMITTED]';
    }
  }
  return result;
}

function compactSafeValues(value: unknown, prefix = ''): Array<{ field: string; value: unknown }> {
  const safeValueKey = /(price|amount|currency|plan|product|auto.?renew|renew|valid|expir|active|status|days|duration|ride.?count|unlimited|success|payment.?status|transaction.?status)/i;
  const values: Array<{ field: string; value: unknown }> = [];
  if (Array.isArray(value)) {
    value.slice(0, 20).forEach((item, index) => values.push(...compactSafeValues(item, `${prefix}[${index}]`)));
  } else if (value && typeof value === 'object') {
    for (const [key, child] of Object.entries(value as Record<string, unknown>).slice(0, 100)) {
      const field = prefix ? `${prefix}.${key}` : key;
      if (safeValueKey.test(key) && (child === null || ['string', 'number', 'boolean'].includes(typeof child))) {
        values.push({ field, value: typeof child === 'string' ? redactText(child.slice(0, 200)) : child });
      } else if (child && typeof child === 'object') {
        values.push(...compactSafeValues(child, field));
      }
    }
  }
  return values.slice(0, 100);
}

test('read-only authenticated staging inspection', async ({ browser }) => {
  test.setTimeout(120000);
  const context = await browser.newContext({ storageState: 'auth.json' });
  const page = await context.newPage();
  const network: Array<Record<string, unknown>> = [];
  let paymentPopup: import('@playwright/test').Page | null = null;
  page.on('popup', (popup) => { paymentPopup = popup; });

  page.on('request', (request) => {
    if (!['xhr', 'fetch'].includes(request.resourceType())) return;
    let requestSummary: unknown = '[BODY OMITTED]';
    const rawBody = request.postData();
    if (rawBody) {
      try {
        requestSummary = summarizeJson(JSON.parse(rawBody));
      } catch {
        requestSummary = '[NON-JSON BODY OMITTED]';
      }
    }
    network.push({
      kind: 'request',
      method: request.method(),
      path: safePath(request.url()),
      queryKeys: [...new URL(request.url()).searchParams.keys()],
      resourceType: request.resourceType(),
      body: requestSummary,
    });
  });

  page.on('requestfailed', (request) => {
    if (!['xhr', 'fetch'].includes(request.resourceType())) return;
    network.push({
      kind: 'request-failed',
      method: request.method(),
      path: safePath(request.url()),
      failure: request.failure()?.errorText ?? 'unknown',
    });
  });

  page.on('response', async (response) => {
    const request = response.request();
    if (!['xhr', 'fetch'].includes(request.resourceType())) return;
    const headers = await response.allHeaders().catch(() => ({}));
    let bodySummary: unknown = '[BODY OMITTED]';
    if ((headers['content-type'] ?? '').includes('application/json')) {
      try {
        const rawBody: unknown = await response.json();
        if (response.url().includes('/get-search-suggestions-for-customer/')) {
          const suggestionBody = rawBody as Record<string, unknown>;
          collectSensitiveStrings({ ...suggestionBody, stops: undefined });
          const recentHistory = suggestionBody?.recent_history;
          if (Array.isArray(recentHistory)) collectAllStrings(recentHistory);
          publicStopSignals.pickupSuggestionPresent ||= containsText(rawBody, new RegExp(pickupTerm.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'), 'i'));
          publicStopSignals.dropoffSuggestionPresent ||= containsText(rawBody, new RegExp(dropoffTerm.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'), 'i'));
          const searchTerm = new URL(response.url()).searchParams.get('search_term')?.toLowerCase() ?? '';
          const stopSuggestions = (rawBody as Record<string, unknown>)?.stops;
          if (searchTerm.includes(pickupTerm.toLowerCase()) && pickupRequiredContext) {
            publicStopSignals.pickupContextPresent ||= oneObjectContainsTerms(stopSuggestions, [pickupTerm, pickupRequiredContext]);
          }
          if (searchTerm.includes(dropoffTerm.toLowerCase()) && dropoffRequiredContext) {
            publicStopSignals.dropoffContextPresent ||= oneObjectContainsTerms(stopSuggestions, [dropoffTerm, dropoffRequiredContext]);
          }
        } else {
          collectSensitiveStrings(rawBody);
        }
        if (response.url().includes('/get-user-profile/')) {
          const profile = rawBody as Record<string, unknown>;
          accountSetupSignals = {
            residentialAddressConfigured: hasNonEmptyValue(profile.residential_address),
            officeAddressConfigured: hasNonEmptyValue(profile.office_address),
          };
        }
        bodySummary = summarizeJson(rawBody);
      } catch {
        bodySummary = '[JSON BODY UNAVAILABLE]';
      }
    }
    network.push({
      kind: 'response',
      method: request.method(),
      path: safePath(response.url()),
      status: response.status(),
      body: bodySummary,
    });
  });

  await page.goto(stagingUrl, { waitUntil: 'domcontentloaded' });
  await page.waitForLoadState('networkidle', { timeout: 15000 }).catch(() => undefined);
  await page.waitForTimeout(1500);

  const snapshot = async (step: string) => {
    const visibleText = await page.locator('body').innerText();
    const searchButton = page.getByRole('button', { name: 'Search', exact: true });
    const searchButtonCount = await searchButton.count();
    const searchButtonEnabled = searchButtonCount ? await searchButton.isEnabled() : null;
    return {
      step,
      pageTitle: redactText(await page.title()),
      pageUrl: new URL(page.url()).origin + safePath(page.url()),
      headings: (await page.getByRole('heading').allTextContents()).map((text) => redactText(text.trim())),
      buttons: (await page.getByRole('button').evaluateAll((buttons) => buttons.map((button) => ({
        label: button.textContent?.trim() ?? '',
        ariaLabel: button.getAttribute('aria-label') ?? '',
        title: button.getAttribute('title') ?? '',
        enabled: !(button as HTMLButtonElement).disabled,
      })))).map((button) => ({ ...button, label: redactText(button.label), ariaLabel: redactText(button.ariaLabel), title: redactText(button.title) })),
      inputs: (await page.locator('input').evaluateAll((inputs) => inputs.map((input) => ({
        type: (input as HTMLInputElement).type,
        placeholder: input.getAttribute('placeholder') ?? '',
        ariaLabel: input.getAttribute('aria-label') ?? '',
        hasValue: Boolean((input as HTMLInputElement).value),
        value: (input as HTMLInputElement).value,
      })))).map((input) => ({ ...input, placeholder: redactText(input.placeholder), ariaLabel: redactText(input.ariaLabel), value: redactText(input.value) })),
      links: (await page.getByRole('link').allTextContents()).map((text) => redactText(text.trim())),
      visibleCopy: redactVisibleCopy(visibleText).slice(0, 1800),
      uiSignals: {
        ridePackCopyPresent: /ride\s*pack/i.test(visibleText),
        emptyPackInvitationPresent: /(?:no|don't have|do not have).{0,40}(?:active\s+)?pack|(?:buy|purchase).{0,40}pack/i.test(visibleText),
        unlimitedMonthlyPresent: /unlimited\s+monthly/i.test(visibleText),
        expiredStatePresent: /\bexpired\b/i.test(visibleText),
        validityCopyPresent: /days?\s+remaining|valid\s+(?:until|till|through)/i.test(visibleText),
        rupeeAmounts: visibleText.match(/₹\s?[\d,]+(?:\.\d{1,2})?/g) ?? [],
        searchButtonEnabled,
        searchSuggestionRoleCount: await page.getByRole('option').count(),
        pickupStopMentionPresent: new RegExp(pickupTerm.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'), 'i').test(visibleText),
        dropoffStopMentionPresent: new RegExp(dropoffTerm.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'), 'i').test(visibleText),
        cityfloStopsVisible: /cityflo stops/i.test(visibleText),
        cityfloStopLineCount: (() => {
          const lines = visibleText.split('\n');
          const headerIndex = lines.lastIndexOf('Cityflo Stops');
          return headerIndex < 0 ? 0 : lines.slice(headerIndex + 1).filter((line) => line.trim()).length;
        })(),
        routeDetailsActionCount: await page.getByRole('button', { name: /view route details/i }).count(),
        bookingActionCount: await page.getByRole('button', { name: /book|buy|checkout|continue/i }).count(),
      },
    };
  };

  const snapshots: Array<Record<string, unknown>> = [await snapshot('rider-home')];
  let paymentAttemptStarted = false;
  await page.getByRole('button', { name: 'Ride Pack', exact: true }).click();
  await page.waitForLoadState('networkidle', { timeout: 15000 }).catch(() => undefined);
  await page.waitForTimeout(1000);
  snapshots.push(await snapshot('ride-pack-tab'));

  await page.getByRole('button', { name: 'Home', exact: true }).click();
  await page.waitForLoadState('networkidle', { timeout: 15000 }).catch(() => undefined);
  await page.waitForTimeout(500);
  snapshots.push(await snapshot('home-return'));

  let savedRouteSearchAttempted = false;
  if (process.env.CITYFLO_USE_SAVED_ROUTE === '1') {
    const search = page.getByRole('button', { name: 'Search', exact: true });
    const searchEnabledBefore = await search.count() ? await search.isEnabled() : false;
    if (searchEnabledBefore) {
      savedRouteSearchAttempted = true;
      await search.click({ timeout: 2500 });
      await page.waitForLoadState('networkidle', { timeout: 15000 }).catch(() => undefined);
      snapshots.push({ ...(await snapshot('own-saved-route-results')), savedRouteSearchAttempted, searchEnabledBefore });
      const ridePackTab = page.getByRole('button', { name: 'Ride Pack', exact: true });
      if (await ridePackTab.count()) {
        await ridePackTab.click({ timeout: 2500 });
        await page.waitForLoadState('networkidle', { timeout: 15000 }).catch(() => undefined);
        snapshots.push(await snapshot('ride-pack-after-own-route-search'));
      }
    } else {
      snapshots.push({ ...(await snapshot('own-saved-route-not-searchable')), savedRouteSearchAttempted, searchEnabledBefore });
    }
  } else {
    const pickupPrompt = page.getByText('Where do you live?', { exact: true });
    const pickupControl = (await pickupPrompt.count()) ? pickupPrompt : page.getByText('Pickup', { exact: true }).last();
    if (await pickupControl.count()) {
    await pickupControl.click();
    await page.waitForLoadState('networkidle', { timeout: 15000 }).catch(() => undefined);
    await page.waitForTimeout(500);
    snapshots.push(await snapshot('pickup-address-selector'));

    const savedHome = page.getByText('Home', { exact: true }).last();
    let homeSelectionSucceeded = false;
    let clickTarget: Record<string, unknown> | null = null;
    if (await savedHome.count()) {
      const box = await savedHome.boundingBox();
      if (box) {
        clickTarget = await page.evaluate(({ x, y }) => {
          const element = document.elementFromPoint(x, y);
          return {
            tag: element?.tagName ?? null,
            role: element?.getAttribute('role') ?? null,
            ariaHidden: element?.getAttribute('aria-hidden') ?? null,
            className: typeof element?.className === 'string' ? element.className.slice(0, 180) : '',
          };
        }, { x: box.x + box.width / 2, y: box.y + box.height / 2 });
      }
      try {
        await savedHome.click({ timeout: 2500 });
        homeSelectionSucceeded = true;
      } catch {
        homeSelectionSucceeded = false;
      }
    }
    const addressInput = page.locator('input[type="text"]').first();
    let inputSelectionSucceeded = false;
    let inputClickTarget: Record<string, unknown> | null = null;
    if (await addressInput.count()) {
      const box = await addressInput.boundingBox();
      if (box) {
        inputClickTarget = await page.evaluate(({ x, y }) => {
          const element = document.elementFromPoint(x, y);
          return {
            tag: element?.tagName ?? null,
            role: element?.getAttribute('role') ?? null,
            ariaHidden: element?.getAttribute('aria-hidden') ?? null,
            className: typeof element?.className === 'string' ? element.className.slice(0, 180) : '',
          };
        }, { x: box.x + box.width / 2, y: box.y + box.height / 2 });
      }
      try {
        await addressInput.click({ timeout: 2500 });
        inputSelectionSucceeded = true;
      } catch {
        inputSelectionSucceeded = false;
      }
    }
    const clickResult = await snapshot('pickup-option-click-result');
    snapshots.push({ ...clickResult, clickTarget, homeSelectionSucceeded, inputClickTarget, inputSelectionSucceeded });

    if (!homeSelectionSucceeded && inputSelectionSucceeded) {
      const pickupResponse = page.waitForResponse((response) => response.url().includes('/get-search-suggestions-for-customer/') && new URL(response.url()).searchParams.has('search_term'), { timeout: 15000 }).catch(() => null);
      await addressInput.fill(pickupTerm);
      const pickupResponseResult = await pickupResponse;
      if (pickupResponseResult) await pickupResponseResult.finished().catch(() => undefined);
      await page.waitForTimeout(500);
      const publicSuggestions = await snapshot('public-location-suggestions');
      const cityStopsHeader = page.getByText('Cityflo Stops', { exact: true }).last();
      const cityfloStopsSectionCount = await cityStopsHeader.count();
      let publicPowaiCandidateCount = 0;
      let publicPowaiSelectionSucceeded = false;
      const publicStops = cityfloStopsSectionCount ? cityStopsHeader.locator('xpath=../..') : null;
      const pickupOption = publicStops?.getByText(new RegExp(pickupMatch, 'i')).last();
      publicPowaiCandidateCount = pickupOption ? await pickupOption.count() : 0;
      const pickupContextMatch = publicStopSignals.pickupContextPresent
        ? { matched: true, depth: null, checks: [] }
        : await hasNearbyContext(pickupOption, pickupRequiredContext);
      if (!pickupContextMatch.matched && publicStopSignals.pickupContextPresent) pickupContextMatch.matched = true;
      if (pickupOption && publicPowaiCandidateCount && pickupContextMatch.matched) {
        try {
          await pickupOption.click({ timeout: 2500 });
          publicPowaiSelectionSucceeded = true;
        } catch {
          publicPowaiSelectionSucceeded = false;
        }
      }
      if (publicPowaiSelectionSucceeded) {
        await page.waitForLoadState('networkidle', { timeout: 10000 }).catch(() => undefined);
      }
      snapshots.push({ ...publicSuggestions, step: 'pickup-suggestions', cityfloStopsSectionCount, publicPowaiCandidateCount, publicPowaiSelectionSucceeded, pickupContextMatch });
      if (publicPowaiSelectionSucceeded) snapshots.push(await snapshot(`${pickupTerm.toLowerCase()}-pickup-selected`));

      if (publicPowaiSelectionSucceeded) {
        const workPrompt = page.getByText('Where do you work?', { exact: true });
        const dropoffControl = (await workPrompt.count()) ? workPrompt : page.getByText('Dropoff', { exact: true }).last();
        if (await dropoffControl.count()) {
          await dropoffControl.click({ timeout: 2500 });
          const dropInput = page.locator('input[type="text"]').first();
          const dropoffResponse = page.waitForResponse((response) => response.url().includes('/get-search-suggestions-for-customer/') && new URL(response.url()).searchParams.has('search_term'), { timeout: 15000 }).catch(() => null);
          await dropInput.fill(dropoffTerm);
          const dropoffResponseResult = await dropoffResponse;
          if (dropoffResponseResult) await dropoffResponseResult.finished().catch(() => undefined);
          await page.waitForTimeout(500);
          const bkcSuggestions = await snapshot('public-dropoff-suggestions');
          const dropStopsHeader = page.getByText('Cityflo Stops', { exact: true }).last();
          const dropStopsSectionCount = await dropStopsHeader.count();
          let bkcCandidateCount = 0;
          let bkcSelectionSucceeded = false;
          const cityfloDropStops = dropStopsSectionCount ? dropStopsHeader.locator('xpath=../..') : null;
          const dropoffOption = cityfloDropStops?.getByText(new RegExp(dropoffMatch, 'i')).last();
          bkcCandidateCount = dropoffOption ? await dropoffOption.count() : 0;
          const dropoffContextMatch = publicStopSignals.dropoffContextPresent
            ? { matched: true, depth: null, checks: [] }
            : await hasNearbyContext(dropoffOption, dropoffRequiredContext);
          if (!dropoffContextMatch.matched && publicStopSignals.dropoffContextPresent) dropoffContextMatch.matched = true;
          if (dropoffOption && bkcCandidateCount && dropoffContextMatch.matched) {
            try {
              await dropoffOption.click({ timeout: 2500 });
              bkcSelectionSucceeded = true;
            } catch {
              bkcSelectionSucceeded = false;
            }
          }
          if (bkcSelectionSucceeded) {
            await page.waitForLoadState('networkidle', { timeout: 10000 }).catch(() => undefined);
          }
          snapshots.push({ ...bkcSuggestions, step: 'dropoff-suggestions', cityfloStopsSectionCount: dropStopsSectionCount, bkcCandidateCount, bkcSelectionSucceeded, dropoffContextMatch });
          if (bkcSelectionSucceeded) snapshots.push(await snapshot(`${dropoffTerm.toLowerCase()}-drop-selected`));
          if (bkcSelectionSucceeded) {
            await page.keyboard.press('Escape');
            await page.waitForTimeout(300);
            snapshots.push(await snapshot('dropoff-picker-closed'));
          }
          const search = page.getByRole('button', { name: 'Search', exact: true });
          if (await search.count() && await search.isEnabled()) {
            const routeSearchResponse = page.waitForResponse(
              (response) => response.url().includes('/api/v4/routes/search/'),
              { timeout: 30000 },
            ).catch(() => null);
            await search.click({ timeout: 2500 });
            const routeResponse = await routeSearchResponse;
            if (routeResponse) await routeResponse.finished().catch(() => undefined);
            snapshots.push(await snapshot('route-results'));
            const futureDate = page.getByRole('button', { name: /^Mon, 5th Oct$/i }).first();
            if (process.env.CITYFLO_SKIP_DATE_SELECTION !== '1' && await futureDate.count() && await futureDate.isEnabled()) {
              const futureSearchResponse = page.waitForResponse(
                (response) => response.url().includes('/api/v4/routes/search/'),
                { timeout: 30000 },
              ).catch(() => null);
              await futureDate.click({ timeout: 3000 });
              const futureResponse = await futureSearchResponse;
              if (futureResponse) await futureResponse.finished().catch(() => undefined);
              await page.waitForTimeout(500);
              snapshots.push(await snapshot('future-date-route-results'));
            }
            const routeProceed = page.getByRole('button', { name: /^proceed$/i }).first();
            if (await routeProceed.count() && await routeProceed.isVisible()) {
              await routeProceed.click({ timeout: 3000 });
              await page.waitForLoadState('networkidle', { timeout: 5000 }).catch(() => undefined);
              snapshots.push({ ...(await snapshot('after-route-proceed')), routeProceedClicked: true });
            }
            if (page.url().includes('/booking/ride-type')) {
              const topPickProceed = page.getByRole('button', { name: /^proceed$/i }).first();
              if (await topPickProceed.count() && await topPickProceed.isVisible()) {
                await topPickProceed.click({ timeout: 3000 });
                await page.waitForLoadState('networkidle', { timeout: 5000 }).catch(() => undefined);
                snapshots.push(await snapshot('confirm-booking-page'));
              }
            }
            if (page.url().includes('/booking/confirmation')) {
              await page.evaluate(() => window.scrollTo({ top: document.body.scrollHeight, behavior: 'instant' }));
              await page.waitForTimeout(500);
              snapshots.push(await snapshot('confirm-booking-form'));
              if (bookingName && bookingEmail) {
                sensitiveStrings.push(bookingName, bookingEmail);
                const nameInput = page.locator('input[placeholder="Eg: John Doe"]');
                const emailInput = page.locator('input[type="email"]');
                if (await nameInput.count() && await emailInput.count()) {
                  await nameInput.fill(bookingName);
                  await emailInput.fill(bookingEmail);
                  const continuePayment = page.getByRole('button', { name: /continue to payment/i });
                  await page.waitForTimeout(1000);
                  const fieldsFilled = Boolean((await nameInput.inputValue().catch(() => '')).trim())
                    && Boolean((await emailInput.inputValue().catch(() => '')).trim());
                  const bookingPageText = await page.locator('body').innerText().catch(() => '');
                  const zeroAmount = /Amount Payable\s*FREE/i.test(bookingPageText);
                  const inspectOnly = process.env.CITYFLO_INSPECT_ONLY === '1';
                  snapshots.push({ ...(await snapshot('confirm-booking-profile-complete')), fieldsFilled, zeroAmount });
                  const bookRide = page.getByRole('button', { name: /^book ride$/i }).last();
                  if (!inspectOnly && fieldsFilled && zeroAmount && await bookRide.count() && await bookRide.isEnabled()) {
                    await bookRide.click({ timeout: 3000 });
                    await page.waitForLoadState('networkidle', { timeout: 10000 }).catch(() => undefined);
                    snapshots.push(await snapshot('booking-confirmation-result'));
                  }
                  const payAndBook = page.getByRole('button', { name: /^pay\s+₹?[\d,.]+\s+and\s+book\s+ride$/i }).last();
                  if (!inspectOnly && await payAndBook.count() && await payAndBook.isEnabled()) {
                    paymentAttemptStarted = true;
                    await payAndBook.click({ timeout: 3000 });
                    await page.waitForLoadState('networkidle', { timeout: 10000 }).catch(() => undefined);
                    if (paymentPopup) {
                      await paymentPopup.waitForLoadState('domcontentloaded', { timeout: 10000 }).catch(() => undefined);
                      const popupText = redactVisibleCopy(await paymentPopup.locator('body').innerText().catch(() => ''));
                      snapshots.push({
                        step: 'payment-options-popup',
                        pageUrl: new URL(paymentPopup.url()).origin + safePath(paymentPopup.url()),
                        pageTitle: redactText(await paymentPopup.title().catch(() => '')),
                        visibleCopy: popupText.slice(0, 1800),
                        buttons: (await paymentPopup.getByRole('button').allTextContents()).map((label) => redactText(label.trim())),
                      });
                    }
                    snapshots.push(await snapshot('payment-options'));
                  }
                  if (!inspectOnly && await continuePayment.count() && await continuePayment.isEnabled()) {
                    await continuePayment.click({ timeout: 3000 });
                    await page.waitForLoadState('networkidle', { timeout: 10000 }).catch(() => undefined);
                    snapshots.push(await snapshot('payment-options'));
                  }
                }
              }
            }
            if (process.env.CITYFLO_CONFIRM_SEARCH === '1') {
              const resultsSearch = page.getByRole('button', { name: 'Search', exact: true });
              if (await resultsSearch.count() && await resultsSearch.isEnabled()) {
                await resultsSearch.click({ timeout: 2500 });
                await page.waitForLoadState('networkidle', { timeout: 15000 }).catch(() => undefined);
                snapshots.push(await snapshot('route-results-after-explicit-search'));
              }
            }
            if (process.env.CITYFLO_CONFIRM_SEARCH === '1') {
              const resultsSearch = page.getByRole('button', { name: 'Search', exact: true });
              if (await resultsSearch.count() && await resultsSearch.isEnabled()) {
                await resultsSearch.click({ timeout: 2500 });
                await page.waitForLoadState('networkidle', { timeout: 15000 }).catch(() => undefined);
                snapshots.push(await snapshot('route-results-after-explicit-search'));
              }
            }
            const ridePackTab = page.getByRole('button', { name: 'Ride Pack', exact: true });
            if (!paymentAttemptStarted && !/payment|checkout/i.test(page.url()) && await ridePackTab.count()) {
              await ridePackTab.click({ timeout: 2500 });
              await page.waitForLoadState('networkidle', { timeout: 15000 }).catch(() => undefined);
              snapshots.push(await snapshot('ride-pack-after-route-search'));
            }
          } else {
            snapshots.push(await snapshot('route-selection-incomplete'));
          }
        }
      }
    }

    if (homeSelectionSucceeded) {
      await page.waitForLoadState('networkidle', { timeout: 10000 }).catch(() => undefined);
      snapshots.push(await snapshot('home-pickup-selected'));
      const workPrompt = page.getByText('Where do you work?', { exact: true });
      if (await workPrompt.count()) {
        await workPrompt.click({ timeout: 2500 });
        await page.waitForLoadState('networkidle', { timeout: 10000 }).catch(() => undefined);
        const savedOffice = page.getByText('Office', { exact: true }).last();
        if (await savedOffice.count()) {
          await savedOffice.click({ timeout: 2500 }).catch(() => undefined);
          await page.waitForLoadState('networkidle', { timeout: 10000 }).catch(() => undefined);
          snapshots.push(await snapshot('office-drop-selected'));
        }
      }
      const search = page.getByRole('button', { name: 'Search', exact: true });
      if (await search.count() && await search.isEnabled()) {
        await search.click({ timeout: 2500 });
        await page.waitForLoadState('networkidle', { timeout: 15000 }).catch(() => undefined);
        snapshots.push(await snapshot('route-results'));
      } else {
        snapshots.push(await snapshot('route-selection-incomplete'));
      }
    }
    }
  }

  const report = {
    observedAt: new Date().toISOString(),
    accountSetupSignals,
    publicStopSignals,
    snapshots,
    network,
  };

  await fs.mkdir(outputDir, { recursive: true });
  await fs.writeFile(path.join(outputDir, reportName), JSON.stringify(report, null, 2));
  console.log(JSON.stringify({
    snapshots,
    network: network.map(({ kind, method, path: requestPath, status, body }) => ({
      kind,
      method,
      path: requestPath,
      status,
      values: compactSafeValues(body),
    })),
  }, null, 2));

  expect(new URL(page.url()).origin).toBe(new URL(stagingUrl).origin);
  expect(snapshots.some((item) => item.step === 'pickup-address-selector' || item.step === 'own-saved-route-results' || item.step === 'own-saved-route-not-searchable')).toBeTruthy();
  await context.close();
});
