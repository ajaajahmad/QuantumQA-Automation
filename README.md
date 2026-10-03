# Cityflo QA Engineer Take-Home

## Assessment status

**Inconclusive; not a pass.** The staging account did not expose an Unlimited Monthly Ride Pack plan or checkout. The purchase, pack activation, Auto-Renew, validity, expiry, renewal, and refund requirements could not be verified. See [DECISION.md](DECISION.md) for the release assessment and [TEST_PLAN.md](TEST_PLAN.md) for the full requirement matrix, assumptions, cuts, and observations.

## Deliverables

- [TEST_PLAN.md](TEST_PLAN.md) — PRD requirements, scenarios, expected behavior, evidence approach, PM questions, scope, constraints, and outcomes.
- [BUGS.md](BUGS.md) — one reproduced P3 saved-address picker issue; other observations remain unresolved.
- [DECISION.md](DECISION.md) — evidence-based release assessment with the decision rule, counterexample, costs, and remaining uncertainty.
- [decisions.jsonl](decisions.jsonl) — consequential decisions and transcript anchors.
- [tests/cityflo/session-inspection.spec.ts](tests/cityflo/session-inspection.spec.ts) — Playwright inspection flow.
- [evidence/latest-run-summary.md](evidence/latest-run-summary.md) — sanitized summary of the latest run.
- [SUBMISSION.md](SUBMISSION.md) — setup, artifact index, and submission boundary.

## Run

Install dependencies with `npm ci`. Authenticate with the normal rider flow and enter OTP directly in the visible browser:

```bash
node_modules/.bin/playwright codegen --save-storage=auth.json https://app.cityflostaging.com
```

Close the browser after login to save local `auth.json`. It is ignored by Git; never share or commit it. Reuse that state to run the inspection:

```bash
CITYFLO_INSPECT_ONLY=1 \
CITYFLO_PICKUP_TERM='Teen Haath Naka' \
CITYFLO_PICKUP_MATCH='Teen Haath Naka' \
CITYFLO_PICKUP_CONTEXT='Central Thane' \
CITYFLO_DROPOFF_TERM=Lakeshore \
CITYFLO_DROPOFF_MATCH=Lakeshore \
node_modules/.bin/playwright test tests/cityflo/session-inspection.spec.ts --reporter=line
```

`CITYFLO_INSPECT_ONLY=1` prevents booking or payment actions. The test is an inspection aid, not a pass/fail suite for Ride Pack behavior. Generated reports, auth state, traces, and payment/session data must remain private.

## Submission boundary

The raw agent session transcript is intentionally excluded from this repository. Cityflo requires the complete original transcript through its session-log upload flow; do not replace it with this summary or commit it here.
