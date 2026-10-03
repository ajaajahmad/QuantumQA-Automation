# Cityflo QA Engineer Assignment — Submission Index

## Status

The assignment is not fully tested: staging did not expose an Unlimited Monthly Ride Pack plan or checkout, so purchase and pack behavior remain unverified. The release assessment is inconclusive and recommends holding approval until the core money/access flow can be tested safely. The raw session transcript is not included in this repository and must be transferred separately through Cityflo's session-log mechanism.

## Deliverables

- [TEST_PLAN.md](TEST_PLAN.md) — requirement matrix, assumptions and PM questions, test scope, safety constraints, and observed results.
- [BUGS.md](BUGS.md) — one reproducible P3 saved-address picker issue; unresolved observations are not filed as bugs.
- [DECISION.md](DECISION.md) — evidence-based inconclusive release assessment, risk rule, counterexample, costs, and missing evidence.
- [decisions.jsonl](decisions.jsonl) — consequential decisions with the quoted user instruction and transcript anchor.
- [tests/cityflo/session-inspection.spec.ts](tests/cityflo/session-inspection.spec.ts) — Playwright staging inspection flow; it reuses only local `auth.json` and redacts saved addresses and account data.
- [evidence/latest-run-summary.md](evidence/latest-run-summary.md) — sanitized summary of the latest UI and network observations.

## Run instructions

Install dependencies with `npm install`. Authenticate through the normal rider flow, entering OTP directly in the visible browser:

```bash
node_modules/.bin/playwright codegen --save-storage=auth.json https://app.cityflostaging.com
```

Close the browser after login so Playwright saves local `auth.json`. Never share or commit that file. Then run the staging inspection:

```bash
CITYFLO_PICKUP_TERM='Teen Haath Naka' \
CITYFLO_PICKUP_MATCH='Teen Haath Naka' \
CITYFLO_PICKUP_CONTEXT='Central Thane' \
CITYFLO_DROPOFF_TERM=Lakeshore \
CITYFLO_DROPOFF_MATCH=Lakeshore \
CITYFLO_INSPECT_ONLY=1 \
node_modules/.bin/playwright test tests/cityflo/session-inspection.spec.ts --reporter=line
```

`CITYFLO_INSPECT_ONLY=1` prevents the harness from clicking booking or payment actions. The current test is an inspection aid, not an automated pass/fail suite for the Ride Pack requirements. Re-check all payment and pack-state evidence manually; do not use real payment methods.

## Not included

- `auth.json`, HAR files, traces, screenshots, session data, and other authentication artifacts.
- Raw agent session transcript. The assignment requires it for final submission, but it has not been exported, uploaded, or submitted under the owner's earlier instruction.
- Raw agent session transcript. It must be uploaded separately as the complete original session file; do not commit it to the repository.
- Evidence for a successful Ride Pack purchase, Auto-Renew, validity, or renewal. Those cases were not reached and are not represented as passed.
