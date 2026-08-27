# Product and engineering handoff

## Status at handoff

The product strategy and interactive prototype are complete. A production Sprint 0 foundation now exists alongside the unchanged prototype; it is ready for clean-checkout CI, provider selection, staging deployment, and structured discovery. `docs/brand-interface-foundation.md` now defines Vincelia as the conditional working masterbrand and translates the product strategy into an implementation-ready interface layout.

Production foundation added on 2026-08-19:

- `apps/web`: Next.js production landing page, early-access flow, and professional shell.
- `services/api`: Java 25 / Spring Boot 4.1 modular monolith with identity, acquisition, patient-boundary, audit, and platform foundations.
- `contracts/openapi` and `packages/api-client`: versioned OpenAPI contract and generated TypeScript client.
- `infra`: local PostgreSQL plus a portable Fly.io deployment template.
- `docs/adr`, `docs/security`, `docs/runbooks`, and `docs/sprint-0-execution-plan.md`: decisions, threat controls, operations, and the two-week execution gate.

The prototype currently demonstrates:

- Nutritionist dashboard, patient list/profile, consultation preparation and notes, nutrition-plan builder, AI-assisted substitutions, and publication feedback.
- Professional growth workspace with social content planning, ethical review cues, and a lightweight lead pipeline.
- Patient web journey covering today's meals, nutritionist-approved substitutions, plan access, adherence, progress, check-ins, and messaging.
- Patient mobile concept covering meal registration, plan browsing, constrained plan assistant, and progress tracking.
- Conversion landing page with positioning, professional-growth narrative, patient value, AI safety, and a simulated early-access form.

All prototype data is fictional and stored only in React component state.

## Foundational decisions already made

| Area | Direction |
| --- | --- |
| MVP architecture | Modular monolith |
| Professional web | Next.js App Router + TypeScript |
| Backend | Java 25 + Spring Boot 4.1 + Spring Modulith |
| Database | PostgreSQL + Flyway |
| API | REST + OpenAPI-generated clients |
| Tenancy | Shared DB/shared schema; Organization boundary and `organization_id` |
| Background work | Transactional outbox + PostgreSQL-backed jobs/JobRunr |
| Patient MVP | Responsive PWA |
| Patient post-MVP | Expo/React Native |
| AI | Provider-independent gateway; structured outputs; auditability; human approval |
| Initial hosting candidate | Vercel + Fly.io GRU + Supabase São Paulo |

These are informed recommendations, not immutable rules. Change them through a short ADR with evidence and impact on the 12–16 week timeline and R$1,000/month ceiling.

## Current repository shape

- Root application: deployable Sites-compatible Next.js/Vinext prototype.
- `app/page.tsx`: current interaction model for all four surfaces.
- `app/globals.css`: current visual language and responsive behavior.
- `.openai/hosting.json`: retains the existing Sites project connection.
- `apps/web`: production Next.js application; do not couple it to prototype state.
- `services/api`: production modular monolith and PostgreSQL integration tests.
- `contracts/openapi` and `packages/api-client`: API source contract and generated web client.
- `docs/`: full product/market/architecture material.

## Prototype-to-production migration strategy

Do not rewrite the prototype into a full system in one pass.

1. Preserve the current deployed prototype as the design baseline.
2. Extract design tokens and reusable UI primitives.
3. Scaffold the Java modular monolith and PostgreSQL development environment.
4. Implement authentication, Organization membership, RBAC, and audit foundations.
5. Deliver the first vertical slice: nutritionist onboarding → patient invitation → consent → patient record.
6. Add assessment/consultation records.
7. Add nutrition-plan drafting, versioning, and publication.
8. Add the patient plan/adherence PWA experience.
9. Add only the first low-risk AI workflows after the underlying records and audit trail exist.
10. Introduce the landing-page lead capture and founder onboarding workflow before inviting the external pilot cohort.

## Recommended immediate work

### Discovery track — starts immediately and runs alongside code

- Conduct structured workflow interviews with the nutritionist co-founder using WebDiet side by side.
- Recruit fewer than five switching-oriented nutritionists and record their current plan-building time, frustrations, migration blockers, and willingness to pay.
- Validate the landing-page promise and early-access call to action.
- Define the smallest migration/import path needed for a WebDiet user to switch.

### Engineering track — Sprint 0

- Decide repository layout for `web`, `api`, and shared OpenAPI artifacts.
- Establish local PostgreSQL, migrations, automated checks, and secrets handling.
- Create the first ADRs and initial domain module boundaries.
- Implement CI with lint, unit tests, integration tests, migration validation, and build.
- Deploy a skeleton environment early, with health checks and observability.
- Produce a threat model focused on tenancy, health data, files, invitations, and audit logs.

## Suggested first production vertical slice

The best first slice is not meal planning. It is identity and the relationship boundary:

1. Nutritionist creates an account and Organization.
2. Nutritionist invites a patient.
3. Patient creates an account and sees privacy/consent information.
4. Patient accepts the professional relationship.
5. Nutritionist sees the patient in the organization-scoped list.
6. Both actions are auditable and covered by tenant-isolation tests.

This slice establishes authentication, authorization, tenancy, invitations, consent, email delivery, audit events, and the foundation needed by every later clinical feature.

## Explicitly deferred

- Microservices, Kubernetes, Kafka, and service mesh.
- Native patient app during the 12–16 week MVP.
- Full social-media publishing integrations.
- Open-ended patient medical chatbot.
- Vector database unless a measured retrieval problem requires it.
- Payments, insurance billing, marketplace, wearables, and broad integrations.
- Autonomous AI changes to nutrition plans or clinical records.

## Product feedback scenarios

Use these scenarios with the nutritionist co-founder and pilot users:

1. Prepare for Camila's follow-up and identify what changed since the previous consultation.
2. Adjust a difficult afternoon snack, verify sources/portions, and publish the new plan.
3. As Camila, find a professional-approved substitution and register the meal.
4. Explain why a meal exists without changing the professional's guidance.
5. Turn a recurring patient question into an ethically reviewed weekly content plan.
6. Capture a lead from social media and identify the next follow-up action.

Record time-on-task, confusion points, missing information, trust concerns, and what users expected to happen next.

## Important open decisions

- Validate and clear the working name Vincelia, then register the selected domain. Ritmera is no longer the fallback after an exact-name collision screen.
- Exact commercial pricing and founder-plan terms.
- Authentication provider choice after validating Brazil-region availability, cost, exportability, and MFA requirements.
- Minimum viable import path for professionals switching from WebDiet or Dietbox.
- Whether the first production deployment should retain the multi-provider Vercel/Fly/Supabase recommendation or use a lower-friction temporary variant.
- Which one or two AI features enter the MVP after safety evaluation and unit-economics validation.
