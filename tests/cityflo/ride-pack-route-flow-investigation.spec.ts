import { expect, test } from '@playwright/test';
import fs from 'node:fs/promises';
import path from 'node:path';

const stagingUrl = 'https://app.cityflostaging.com';
const outputPath = path.resolve('evidence/ride-pack-route-flow-authenticated.json');

test.use({
  storageState: 'auth.json',
  screenshot: 'off',
  trace: 'off',
  video: 'off',
});

function safePath(rawUrl: string): string {
  return new URL(rawUrl).pathname
    .replace(/[0-9a-f]{8}-[0-9a-f-]{27,}/gi, ':id')
    .replace(/\b\d{8,}\b/g, ':id');
}

async function responseShape(response: import('@playwright/test').Response): Promise<Record<string, unknown>> {
  const headers = await response.allHeaders().catch(() => ({} as Record<string, string>));
  const contentType = headers['content-type'] ?? '';
  if (!contentType.includes('application/json')) return {};

  try {
    const body: unknown = await response.json();
    const featureKeyPaths: string[] = [];
    const arrayCounts: Array<{ keyPath: string; count: number }> = [];
    const featureFieldShapes: Array<{ keyPath: string; type: string; count?: number }> = [];
    const visit = (value: unknown, prefix = '$', depth = 0): void => {
      if (/(cards_data|morning_vsp|evening_vsp|plans|no_routes_message)/i.test(prefix)) {
        featureFieldShapes.push({
          keyPath: prefix,
          type: value === null ? 'null' : Array.isArray(value) ? 'array' : typeof value,
          ...(Array.isArray(value) ? { count: value.length } : {}),
        });
      }
      if (depth > 6 || value === null || typeof value !== 'object') return;
      if (Array.isArray(value)) {
        if (/(route|card|plan|pack|result|data)/i.test(prefix)) {
          arrayCounts.push({ keyPath: prefix, count: value.length });
        }
        value.slice(0, 20).forEach((item, index) => visit(item, `${prefix}[${index}]`, depth + 1));
        return;
      }
      for (const [key, child] of Object.entries(value as Record<string, unknown>)) {
        const keyPath = `${prefix}.${key}`;
        if (/(route|card|plan|pack|subscription|unlimited|fare|price|error|status|result)/i.test(keyPath)) {
          featureKeyPaths.push(keyPath);
        }
        visit(child, keyPath, depth + 1);
      }
    };
    visit(body);
    return {
      featureKeyPaths: [...new Set(featureKeyPaths)].slice(0, 100),
      arrayCounts: arrayCounts.slice(0, 100),
      featureFieldShapes: featureFieldShapes.slice(0, 100),
    };
  } catch {
    return { jsonBodyReadable: false };
  }
}

test('read-only investigation of the Ride Pack route form submission', async ({ browser }) => {
  test.setTimeout(60000);
  const context = await browser.newContext({ storageState: 'auth.json' });
  const page = await context.newPage();
  const network: Array<Record<string, unknown>> = [];
  let phase = 'home-load';
  let routeSearchOutcome: Record<string, unknown> | null = null;
  const sessionResponseStatuses: Array<{ path: string; status: number }> = [];

  page.on('request', (request) => {
    network.push({
      phase,
      kind: 'request',
      method: request.method(),
      resourceType: request.resourceType(),
      path: safePath(request.url()),
    });
  });
  page.on('requestfailed', (request) => {
    network.push({
      phase,
      kind: 'request-failed',
      method: request.method(),
      resourceType: request.resourceType(),
      path: safePath(request.url()),
      failure: request.failure()?.errorText ?? 'unknown',
    });
  });
  page.on('requestfinished', (request) => {
    network.push({
      phase,
      kind: 'request-finished',
      method: request.method(),
      resourceType: request.resourceType(),
      path: safePath(request.url()),
    });
  });
  page.on('response', async (response) => {
    const request = response.request();
    if (/get-user-profile|get-home-data-main/.test(response.url())) {
      sessionResponseStatuses.push({ path: safePath(response.url()), status: response.status() });
    }
    network.push({
      phase,
      kind: 'response',
      method: request.method(),
      resourceType: request.resourceType(),
      path: safePath(response.url()),
      status: response.status(),
      ...(await responseShape(response)),
    });
  });

  const snapshot = async (step: string) => {
    const search = page.getByRole('button', { name: 'Search', exact: true });
    const routeButtons = await page.getByRole('button').evaluateAll((buttons) => buttons.map((button) => ({
      label: button.textContent?.trim() ?? '',
      enabled: !(button as HTMLButtonElement).disabled,
    })).filter(({ label }) => /^(Search|Ride Pack|Proceed|Buy|Continue to Payment)$/i.test(label)));
    const inputs = await page.locator('input').evaluateAll((elements) => elements.map((element) => ({
      type: (element as HTMLInputElement).type,
      hasValue: Boolean((element as HTMLInputElement).value.trim()),
    })));
    const bodyText = await page.locator('body').innerText().catch(() => '');
    return {
      step,
      urlPath: new URL(page.url()).pathname,
      titleIsCityflo: /cityflo/i.test(await page.title()),
      searchButtonCount: await search.count(),
      searchEnabled: await search.count() ? await search.isEnabled() : false,
      routeButtons,
      inputs,
      uiSignals: {
        routeResultActionPresent: await page.getByRole('button', { name: /^proceed$/i }).count() > 0,
        unlimitedMonthlyCopyPresent: /unlimited\s+monthly/i.test(bodyText),
        rupeePricePresent: /₹\s?\d[\d,]*/.test(bodyText),
        checkoutControlPresent: /checkout|continue to payment|buy now/i.test(bodyText),
      },
    };
  };

  await page.goto(stagingUrl, { waitUntil: 'domcontentloaded' });
  await page.waitForLoadState('networkidle', { timeout: 12000 }).catch(() => undefined);
  await page.waitForResponse((response) => response.url().includes('/get-user-profile/'), { timeout: 8000 }).catch(() => null);
  expect(new URL(page.url()).pathname).not.toBe('/login');
  expect(sessionResponseStatuses.some((item) => item.path.includes('/get-user-profile/') && item.status === 200)).toBeTruthy();
  const snapshots: Array<Record<string, unknown>> = [await snapshot('home')];

  phase = 'ride-pack-entry';
  await page.getByRole('button', { name: 'Ride Pack', exact: true }).click();
  await page.waitForLoadState('networkidle', { timeout: 12000 }).catch(() => undefined);
  snapshots.push(await snapshot('ride-pack-entry'));
  const planRequestsBeforeRouteSearch = network.filter((item) =>
    item.kind === 'request' && typeof item.path === 'string' && item.path.includes('get-lite-pack-details-with-plans'),
  ).length;

  const search = page.getByRole('button', { name: 'Search', exact: true });
  let routeSearchClicked = false;
  if (await search.count() && await search.isEnabled()) {
    phase = 'ride-pack-search-submit';
    const routeResponsePromise = page.waitForResponse((response) =>
      new URL(response.url()).pathname.includes('/routes/search/'), { timeout: 30000 },
    ).catch(() => null);
    routeSearchClicked = true;
    await search.click();
    const routeResponse = await routeResponsePromise;
    if (routeResponse) {
      await routeResponse.finished().catch(() => undefined);
      routeSearchOutcome = {
        receivedResponse: true,
        status: routeResponse.status(),
        resourceType: routeResponse.request().resourceType(),
        ...(await responseShape(routeResponse)),
      };
    } else {
      routeSearchOutcome = { receivedResponse: false, waitTimedOut: true };
    }
    await page.waitForLoadState('networkidle', { timeout: 5000 }).catch(() => undefined);
    snapshots.push(await snapshot('after-ride-pack-search-submit'));
  }

  const report = {
    observedAt: new Date().toISOString(),
    scope: 'Read-only route form inspection; no stop values edited and no booking, checkout, or payment action clicked.',
    routeSearchClicked,
    routeSearchOutcome,
    planRequestsBeforeRouteSearch,
    sessionResponseStatuses,
    snapshots,
    network,
  };
  await fs.mkdir(path.dirname(outputPath), { recursive: true });
  await fs.writeFile(outputPath, JSON.stringify(report, null, 2));

  expect(new URL(page.url()).origin).toBe(new URL(stagingUrl).origin);
  await context.close();
});
