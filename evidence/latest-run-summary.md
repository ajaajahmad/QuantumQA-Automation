# Sanitized Cityflo Staging Evidence Summary

**Run window:** 2026-10-03 20:24–20:31 IST

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

## Separate saved-address observation

The saved-address picker issue in `BUGS.md` was reproduced in earlier Playwright runs: a normal click on the saved Home row was intercepted by a full-screen `aria-hidden=true` overlay. Public-stop search remained a workaround. The original raw run report was under ignored `test-results/` and is not part of this submission package; no authentication artifact is included here.
