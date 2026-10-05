# Sanitized Cityflo Staging Evidence Summary

**Historical run window:** 2026-10-03 20:24–20:31 IST

**Account:** Owner's own rider account; personal fields and saved addresses omitted.

**Artifacts:** Raw Playwright output and storageState remain local and ignored by Git.

## Observed

- Manual OTP login refreshed the local Playwright storageState.
- Route suggestion requests returned HTTP 200. A route search for the owner-directed Teen Haath Naka→Lakeshore locations returned HTTP 200 and displayed a route card with fares starting at ₹1.
- The visible stop text identified Teen Haath Naka and “In front of Lakeshore mall (Viviana) before Jupiter Hospital.” The Wagle Estate locality was not independently present in the captured stop context.
- Selecting Mon, 5 Oct changed the card to Centrum IT Park→Tattvagyan Vidhyapith while the search header retained the selected search locations. The cause and intended behavior are unresolved.
- Proceed reached a one-way ride confirmation page. Its payment summary remained “Calculating…” and Continue to Payment was disabled. The profile fields were blank in the snapshot after the provided details were entered.
- The map displayed `AuthFailure`. No booking, checkout, payment, pack issuance/state, or renewal request was observed. No booking or payment action was clicked.
- The Ride Pack page displayed no Unlimited Monthly plan or active pack.

## Result

This run did not reach an Unlimited Monthly Ride Pack plan or checkout. It does not establish a pass or fail for pack price, purchase, issuance, Auto-Renew, validity, expiry, renewal, or refunds. No payment attempt occurred.

## Current preflight — 2026-10-05 20:54–21:15 IST

**Account:** Owner’s authenticated rider session; auth state is local and ignored.

### Observed

- Home and `/rides` rendered. `get-user-profile` and `get-home-data-main` returned HTTP 200; the latter reported `has_active_subscription: false`.
- The Ride Pack view showed a route form without a plan list, empty-pack invitation, price, active pack, validity, or checkout control.
- In the earlier saved-route home-search probe, navigation reached `/search-results` but the rendered content remained the home route form; that harness run did not capture a route-search XHR/fetch request. A later focused probe clicked Search from the Ride Pack tab and captured the request and response described below.
- Cityflo pickup suggestions appeared for Powai and Andheri East. BKC suggestions appeared as generic location results without a Cityflo Stops group; no BKC suggestion was selected. A Lakeshore result did not verify Wagle Estate context and was not selected.
- No booking, checkout, payment, purchase, pack-state, or renewal request was captured in the initial preflight probes. No payment option appeared, so no purchase was attempted.
- Four read-only preflight tests passed. They check authenticated entry, safe stop suggestion handling, and that checkout does not appear before a supported route is selected. The isolated TypeScript check for the new spec passed; workspace-wide `tsc --noEmit` stops on TS1005 at `src/pages/user/CurrencyAlertPage.ts:17` (unrelated to this change).

### Inferred

- This account/session did not expose a supported route-to-plan path during the tested searches. That does not establish whether a plan exists on another supported route or whether the feature is broken.

### Could not verify

- Plan list and ₹3,000 price, sandbox payment availability, purchase success/failure, pack issuance/binding, unlimited usage, Auto-Renew default or persistence, validity, expiry, cancellation cutoff, renewal success/failure, retry/notification behavior, and refunds.
- Renewal and renewal-failure behavior were not tested; no date or backend manipulation, forced expiry, induced failure, or wait for monthly renewal was used.

### Artifacts

- Earlier raw sanitized reports under ignored `test-results/cityflo/` were cleared by subsequent Playwright runs. The persistent reports are listed in the focused diagnosis below.
- Cityflo tests set tracing off. The existing Playwright report archive’s trace was from unrelated non-Cityflo tests; there is no Cityflo trace or HAR. No payment, pack, or renewal request occurred.
- The new tests are in `tests/cityflo/ride-pack-entry-preflight.spec.ts`; passing these readiness checks does not mean the Ride Pack requirements pass.

## Focused route-flow diagnosis — authenticated, 2026-10-05 22:29 IST

This probe used the route already shown by the Ride Pack form; it changed no pickup/drop-off values and clicked only the visible Search control.

### Observed

- Profile and home endpoints returned HTTP 200. Ride Pack entry showed its route form, with no plan or checkout.
- Search sent `POST /api/v4/routes/search/` (XHR), which returned HTTP 200. The sanitized response schema shows `cards_data.morning_vsp: null` and `cards_data.evening_vsp: null`.
- The resulting `/search-results` UI had no Proceed action, price, or plan. This matches the null route-card fields; there is no evidence that route cards existed but failed to render.
- No request to the separate Ride Pack plan-details endpoint was made. The staging bundle identifies `GET /api/v2/rides/get-lite-pack-details-with-plans/`; its UI call accepts `start_stop_info_pk`, `end_stop_info_pk`, and optional ride/protection fields. No values were guessed or sent manually.
- `/api/ride-protect/v1/summary/` returned 404. The loaded readiness helper treats either a settled success or error as ready, so this response alone does not explain why the plan request was absent.
- The exact stop pair and eligibility reason cannot be established: the route request body/stop IDs were not retained. No specific supported route was found.

### Classification

- **Route search request sent:** Yes.
- **Route response has cards:** No; both morning/evening card fields were null.
- **Route response contains Ride Pack plans:** No; it contains route-search fields only. Plan details use a separate endpoint that was not requested.
- **UI failed to render existing route data:** Not supported by this evidence; the backend returned null card fields and the UI showed no Proceed action.
- **Underlying eligibility/route cause:** Unresolved. Do not infer a route or file this as a confirmed product defect.

### Confidence and release impact

High confidence in the request/status/null-card observation; medium confidence that this explains the missing plan in this run; low confidence in why the service returned no route for the owner account. This does not change the release recommendation: keep it inconclusive and do not approve the money/access flow without a supported route, plan response, and controlled sandbox purchase.

### Evidence and limits

- Confirmed sanitized network record: `evidence/ride-pack-route-flow-authenticated.json`. Initial probe that redirected to `/login`: `evidence/ride-pack-route-flow-investigation.json`.
- No Cityflo trace/HAR exists. The workspace contains no Cityflo frontend source; only exact hashed staging bundles loaded in the browser were inspected.

## Separate saved-address observation

The saved-address picker issue in `BUGS.md` was reproduced in earlier Playwright runs: a normal click on the saved Home row was intercepted by a full-screen `aria-hidden=true` overlay. Public-stop search remained a workaround. The original raw run report was under ignored `test-results/` and is not part of this submission package; no authentication artifact is included here.
