# Sprint 0 execution plan

- Duration: 10 working days
- Engineering capacity: one engineer
- Clinical and discovery owner: nutritionist co-founder
- Objective: make the first production vertical slice safe to start, not ship clinical workflows

## Sprint outcome

At the end of Sprint 0, the team can deploy a production-shaped skeleton, authenticate a fictional professional, resolve the professional's Organization on the server, run forward migrations, prove cross-tenant access is denied, and recruit the first switching-oriented discovery cohort. No real health data is accepted yet.

## Workstreams and ownership

| Workstream | Owner | Deliverable | Current state |
| --- | --- | --- | --- |
| Repository foundation | Engineer | Independent web, API, contracts, generated client, local infrastructure | Complete locally |
| Identity and tenancy | Engineer | Supabase PKCE/SSR, OIDC resource server, Membership-based Organization resolution, audience/MFA and negative isolation tests | Provider and code decision complete; hosted project remains |
| Data foundation | Engineer | PostgreSQL schema, forward Flyway migration, audit and outbox tables | Complete locally; managed restore drill remains |
| Public validation | Engineer + nutritionist | Production landing promise and consent-aware early-access capture | Complete locally; rate limit and staging deployment remain |
| Security and operations | Engineer | Threat model, logging policy, health checks, CI, secrets contract | Free Render Blueprint complete; provider variables, staging alerts, and backup evidence remain |
| Switching discovery | Nutritionist + engineer observer | At least five interviews and an import-contract recommendation | Guide ready; interviews remain |

## Ten-day sequence

### Days 1–2 — reproducible foundation

- Confirm Node 22.19 and Java 25 toolchains.
- Run the web/client checks and API verification from a clean checkout.
- Start local PostgreSQL and prove Flyway can initialize an empty database.
- Review ADR-0001 through ADR-0004 and record any founder objections.

Evidence: green CI-equivalent checks, migration history, accepted ADRs.

### Days 3–4 — identity and tenant boundary

- Use Supabase Auth behind the standard OIDC/JWT adapter; configure local and staging applications.
- Exercise sign-in, token expiry, missing membership, revoked membership, and cross-Organization patient lookup with fictional data.
- Add deployment protection that rejects `dev` or `test` profiles outside local and CI environments.

Evidence: authenticated staging session and passing negative authorization cases.

### Days 5–6 — staging and operations

- Use the temporary $0 Render Free + Supabase Free staging shape recorded in ADR-0005.
- Deploy `apps/web` and `services/api` with a São Paulo Supabase project and fictional data only.
- Configure secrets outside source control, readiness checks, structured operational logs, and error scrubbing.
- Apply an edge/IP rate limit to public early-access capture.

Evidence: staging URLs, health-check history, cost estimate, and a scrubbed test error.

### Days 7–8 — LGPD and recovery gate

- Review the threat model with both founders and assign every before-beta control.
- Document early-access purpose, retention, deletion, and contact workflow.
- Run a restore rehearsal into an empty non-production database and re-run tenant-isolation tests.
- Verify that logs, analytics, outbox events, and screenshots contain no restricted data.

Evidence: timed restore record, updated threat-model owners, and data inventory.

### Days 1–9 in parallel — switching discovery

- Recruit 8–12 switching-oriented prospects to obtain at least five completed sessions; keep the eventual external pilot under five practices.
- Observe a real workflow in the professional's current tool without collecting identifiable patient material.
- Measure preparation and plan-adjustment time, context switches, migration blockers, trust concerns, and willingness to pay.
- Draft the canonical import contract and select the smallest switching path for Sprint 1.

Evidence: five de-identified interview notes, evidence table, and import recommendation.

### Day 10 — exit review

- Demonstrate public capture, professional sign-in, server-resolved Organization context, and denied cross-tenant access.
- Review run-rate, unresolved security work, and discovery evidence.
- Commit or reject the Sprint 1 vertical slice: professional onboarding → patient invitation → consent → accepted relationship.

## Exit gates

Sprint 0 is complete only when all gates are true:

- Java 25 CI and web/client checks pass from a clean checkout.
- A staging deployment uses fictional data and has readiness monitoring.
- OIDC issuer and Organization membership resolution are demonstrated; the client cannot choose a tenant identifier.
- Forward migration and database restoration are exercised and recorded.
- Tenant-isolation and missing/revoked-membership tests pass.
- Public lead capture has abuse controls, a consent text version, retention ownership, and no sensitive event payload.
- At least five switching interviews produce a documented Sprint 1 import decision.
- Monthly staging and MVP infrastructure estimates remain below the operating ceiling.

## Explicit non-goals

- No real patient or clinical data.
- No meal-plan builder, messaging, payments, native app, social publishing, or production AI calls.
- No microservices, Redis, Kafka, Kubernetes, GraphQL, or vector database.
- No autonomous clinical decisions.

## Sprint 1 decision packet

The day-10 review should produce one page containing the selected identity provider, deployment shape and cost, import format, top three switching blockers, tenant/security evidence, and a go/no-go recommendation for the first relationship vertical slice.
