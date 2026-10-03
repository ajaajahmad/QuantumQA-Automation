# Verified Bugs

Only issues reproduced against the staging app are listed here. Personal data and authentication material are omitted.

## CF-001 — Saved address rows cannot be selected in the address picker

- **Severity:** P3 — Normal use is degraded, with a manual stop search available as a workaround.
- **Area:** Rider home route search / pickup address selector.
- **Environment:** Cityflo staging, authenticated own rider account, desktop Chromium via Playwright.
- **Precondition:** Own account has a saved Home address configured.
- **Steps to reproduce:**
  1. Open Home while signed in.
  2. Open the pickup selector using “Where do you live?”.
  3. Click the saved Home row using a normal pointer click.
- **Expected:** The saved address is selected and the selector closes or advances.
- **Actual:** The click is intercepted by a full-screen `aria-hidden=true` overlay; the saved row does not select. The same interception was observed in repeated attempts. No forced click was used.
- **Reproducibility:** Reproduced in multiple Playwright runs (headed and headless).
- **Workaround:** Type a public Cityflo stop into the address search and select the returned stop. The public Powai suggestion was selected successfully.
- **Evidence:** Reproduced in multiple earlier Playwright runs. The sanitized observation is summarized in [evidence/latest-run-summary.md](evidence/latest-run-summary.md); the original raw report was stored under ignored `test-results/` and is not included in this package. No screenshot, trace, address, phone number, token, or session data is included.
- **PRD impact:** Route setup is a prerequisite to reaching Ride Pack plans. This issue can impede the flow for riders relying on saved addresses; the manual search workaround makes it non-blocking for this assignment's release decision unless broader impact is established.
