import { expect, test } from '@playwright/test';

const stagingUrl = 'https://app.cityflostaging.com';

test.use({
  storageState: 'auth.json',
  screenshot: 'off',
  trace: 'off',
  video: 'off',
});

async function openHome(page: import('@playwright/test').Page): Promise<void> {
  await page.goto(stagingUrl, { waitUntil: 'domcontentloaded' });
  await expect(page.getByRole('button', { name: 'Ride Pack', exact: true })).toBeVisible();
}

async function selectPowaiPickup(page: import('@playwright/test').Page): Promise<void> {
  await page.getByText('Pickup', { exact: true }).last().click();
  const input = page.locator('input[type="text"]').first();
  await input.click();
  const suggestionResponse = page.waitForResponse((response) =>
    response.url().includes('/get-search-suggestions-for-customer/')
      && new URL(response.url()).searchParams.get('search_term')?.toLowerCase().includes('powai') === true,
  );
  await input.fill('Powai');
  expect((await suggestionResponse).status()).toBe(200);

  const cityfloStops = page.getByText('Cityflo Stops', { exact: true }).last();
  await expect(cityfloStops).toBeVisible();
  const option = cityfloStops.locator('xpath=../..').getByText(/Powai/i).last();
  await expect(option).toBeVisible();
  await option.click();
  await expect(page.getByText('Bayroute Powai', { exact: true })).toBeVisible();
}

test('authenticated rider reaches the Ride Pack route entry page', async ({ page }) => {
  const profileResponsePromise = page.waitForResponse((response) =>
    response.url().includes('/get-user-profile/'),
  );
  const homeResponsePromise = page.waitForResponse((response) =>
    response.url().includes('/get-home-data-main/'),
  );

  await openHome(page);
  expect((await profileResponsePromise).status()).toBe(200);
  const homeResponse = await homeResponsePromise;
  expect(homeResponse.status()).toBe(200);
  const homeData = await homeResponse.json() as { has_active_subscription?: boolean };
  expect(homeData.has_active_subscription).toBe(false);

  await page.getByRole('button', { name: 'Ride Pack', exact: true }).click();
  await expect(page).toHaveURL(/\/rides$/);
  await expect(page.getByRole('button', { name: 'Search', exact: true })).toBeVisible();
  await expect(page.getByText(/Unlimited Monthly/i)).toHaveCount(0);
});

test('pickup autocomplete exposes and accepts a Cityflo stop', async ({ page }) => {
  await openHome(page);
  await selectPowaiPickup(page);

  // Selection is kept in the page only; this test does not submit a route or booking.
  await expect(page.getByRole('button', { name: 'Search', exact: true })).toBeEnabled();
});

test('non-Cityflo BKC place suggestions are not mistaken for a route stop', async ({ page }) => {
  const routeOrMoneyRequests: string[] = [];
  page.on('request', (request) => {
    if (/routes\/search|checkout|payment|booking/i.test(new URL(request.url()).pathname)) {
      routeOrMoneyRequests.push(new URL(request.url()).pathname);
    }
  });

  await openHome(page);
  await selectPowaiPickup(page);

  await page.getByText('Dropoff', { exact: true }).last().click();
  const input = page.locator('input[type="text"]').first();
  const suggestionResponse = page.waitForResponse((response) =>
    response.url().includes('/get-search-suggestions-for-customer/')
      && new URL(response.url()).searchParams.get('search_term')?.toLowerCase().includes('bkc') === true,
  );
  await input.fill('BKC');
  expect((await suggestionResponse).status()).toBe(200);

  await expect(page.getByText(/Bandra Kurla Complex/i).first()).toBeVisible();
  await expect(page.getByText('Cityflo Stops', { exact: true })).toHaveCount(0);
  expect(routeOrMoneyRequests).toEqual([]);
});

test('Ride Pack without a selected supported route exposes no checkout controls', async ({ page }) => {
  const moneyRequests: string[] = [];
  page.on('request', (request) => {
    if (/checkout|payment|purchase|subscription|renew/i.test(new URL(request.url()).pathname)) {
      moneyRequests.push(new URL(request.url()).pathname);
    }
  });

  await openHome(page);
  await page.getByRole('button', { name: 'Ride Pack', exact: true }).click();
  await expect(page).toHaveURL(/\/rides$/);
  await expect(page.getByRole('button', { name: 'Search', exact: true })).toBeVisible();
  await expect(page.getByRole('button', { name: /buy|checkout|continue to payment/i })).toHaveCount(0);
  await expect(page.getByText(/₹\s?\d[\d,]*/)).toHaveCount(0);
  expect(moneyRequests).toEqual([]);
});
