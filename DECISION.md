# Release Decision — Unlimited Monthly Ride Pack

## Decision

**Inconclusive; do not approve the Ride Pack release from this test run.** The run did not reach a Ride Pack plan, checkout, payment gateway, or issued pack. It therefore provides no evidence that the purchase, Auto-Renew, validity, or renewal requirements pass or fail.

## Repeatable rule

Do not approve a release that moves rider money or grants time-limited access until one controlled sandbox purchase demonstrates that the displayed price and Auto-Renew choice match the order/payment request, a successful payment issues the correct route-bound pack, and the pack state matches the confirmation UI. Renewal state and scheduling must also be inspected through safe evidence; do not simulate a monthly renewal by changing dates or backend state.

## Counterexample and cost

This rule can hold a release even when the feature itself is correct because staging did not expose an accessible plan or checkout in this run. Keep the hold because the cost of delaying a commuter product is borne by Cityflo, while an incorrect charge or missing pack would be borne directly by riders. A delayed release is preferable to claiming unverified money and access behavior is safe.

## Evidence that changes this decision

Re-run on an accessible staging route and plan. Inspect checkout first, then use one unmistakable sandbox test method for one controlled purchase. Capture the redacted order/payment request and response, pack issuance/state response, and matching UI. Verify Auto-Renew defaults OFF and that any chosen setting persists in the saved pack state. Review renewal schedule/state without forcing an attempt, date, or failure. Any payment or renewal failure handling not naturally observable remains unverified.

## What is observed, inferred, and unverified

- **Observed:** Own-account login and route search worked. The route-search endpoint returned HTTP 200. The user-directed Teen Haath Naka→Lakeshore result displayed “In front of Lakeshore mall (Viviana) before Jupiter Hospital” with a fare starting at ₹1; Wagle Estate was not independently visible in the stop metadata. Changing the selected ride date showed a different route card (Centrum IT Park→Tattvagyan Vidhyapith) while the search header retained the requested stops. Proceed reached a one-way ride booking confirmation, where the payment summary remained “Calculating…” and Continue to Payment was disabled. The provided profile details were blank in the post-entry snapshot. No booking/payment action or corresponding network request occurred. The Ride Pack page showed no plan or active pack. The user reported Google Maps `AuthFailure` on search results and confirmation pages.
- **Inferred:** The observed path did not make an Unlimited Monthly Ride Pack purchase available. The route/date change and uncalculated one-way fare are unresolved observations, not confirmed defects in the pack feature. The map rendering error may be an environment configuration issue, but its cause was not independently checked.
- **Unverified:** Plan and checkout pricing, payment method/sandbox availability, purchase success/failure, pack issuance and route binding, validity, unlimited usage, Auto-Renew enrollment/default/persistence/cancellation, renewal success/failure, expiry, refunds, and any release behavior.

## Known defect and shipping risk

`BUGS.md` contains one reproducible P3 issue: saved address rows are not selectable by a normal click, with manual stop search as a workaround. No other issue is classified as a confirmed Ride Pack bug. The map error and unresolved booking transition are recorded as observations, not Ride Pack defects.

## What is knowingly shipping

This test run does not clear the Ride Pack for release; it also does not confirm a Ride Pack defect. If the product ships without further evidence, purchase, pack issuance, Auto-Renew, validity, and renewal behavior would ship unverified. The uncomfortable choice is to hold approval despite no confirmed pack bug; keep the hold because the rider's payment and access risks were not tested.

No release approval is given by this report. A product release decision remains with the product owner after the missing Ride Pack evidence is collected.

## 2026-10-05 live preflight update

The separate October 5 run still supports an **inconclusive** assessment. The authenticated session showed no active subscription, and Ride Pack displayed only its route form. Tested public-stop searches did not produce a supported pickup/drop-off pair that reached a route result or plan. No checkout or payment option appeared, so the requested one sandbox purchase was not attempted. The timebox was narrowed to avoid selecting ambiguous place suggestions or continuing arbitrary route permutations.

### Observed

- Profile and home data requests returned HTTP 200; home data reported `has_active_subscription: false`.
- Powai and Andheri East had Cityflo pickup suggestions. BKC results lacked a Cityflo Stops group. The Lakeshore suggestions did not verify the requested Wagle Estate context.
- The earlier saved-route Home search changed the URL to `/search-results` but showed the home route form; that probe captured no route-search XHR/fetch request. A separate authenticated Search from the Ride Pack route form sent `POST /api/v4/routes/search/`, which returned HTTP 200 with `cards_data.morning_vsp: null` and `cards_data.evening_vsp: null`. The UI showed no route card or Proceed action.
- The loaded Ride Pack bundle contains a separate plan-details API client, but no request to it was observed; the route-search payload contained no plan fields. Four read-only entry/precondition tests and the route-form diagnostic passed.

### Inferred

- The authenticated search returned no route cards, so the plan lookup was not reached. This is evidence for an empty route result in this attempt; it is not evidence that the search rendered available cards incorrectly or that a Ride Pack defect is confirmed. The exact route eligibility cause remains unknown because request-body stop IDs were intentionally not retained.

### Could not verify

- Displayed/charged price, sandbox transaction, payment status, pack issuance and route binding, Auto-Renew default/persistence/cancellation, validity, and renewal scheduling.
- Renewal success and renewal failure, retries, notifications, and access during a failure are explicitly **could not verify**. No failure or expiry was induced, and no date was changed.
- Expiry/lapsed state, refunds, and unlimited ride usage also remain unverified.

CF-001 remains the only confirmed bug. No new issue from the inaccessible route flow was added to `BUGS.md` without a reproducible requirement-level failure.
