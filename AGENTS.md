# Codex project instructions

## Product context

Build a Brazil-first SaaS platform for nutritionists and patients. The system language and visible customer copy are pt-BR. Engineering conversation and technical documentation should remain in English unless the founders explicitly request otherwise.

The founders are:

- Igor: solo software engineer for the MVP, experienced with Java, Spring, React, TypeScript, and Node.js.
- Nutritionist co-founder: currently uses WebDiet and owns clinical workflow validation, pilot recruitment, and professional judgment.

MVP constraints:

- First real launch target: 12–16 weeks.
- Development team: one engineer plus the nutritionist co-founder.
- First six months' operational budget: up to R$1,000/month, excluding salaries.
- Early external pilot group: fewer than five nutritionists.
- Initial paying audience: nutritionists actively considering switching from an existing platform.

## Mandatory reading order

Before making product or architecture decisions:

1. Read `docs/HANDOFF.md`.
2. Read relevant sections of `docs/product-strategy.md`.
3. For UI changes, read the relevant surface in `docs/prototype-brief.md` and inspect the current prototype.

Do not attempt to load the entire strategy into every task. Read the sections relevant to the current issue.

## Product principles

- The nutritionist remains the decision-maker. AI assists; it does not diagnose, prescribe independently, or replace professional judgment.
- Professional growth is part of the core journey: content planning, lead organization, ethical social-media assistance, and patient acquisition matter alongside clinical workflows.
- Patient adherence and understanding matter more than producing a technically perfect but hard-to-follow plan.
- Optimize for fast, low-friction workflows and excellent mobile usability.
- Treat LGPD-sensitive health data as a first-order architecture concern.
- Keep the MVP aggressively scoped. Validate behavior before building advanced automation.

## Architecture guardrails

- Start with a modular monolith. Do not introduce microservices, Kubernetes, Kafka, Redis, GraphQL, or a vector database without measured evidence that the current architecture cannot meet a requirement.
- Target backend: Java 25, Spring Boot 4.1, Spring Modulith.
- Target database: PostgreSQL with Flyway migrations.
- Target API: versioned REST with OpenAPI; generate the TypeScript client.
- Model every professional as an Organization, even for a solo practice. Organization-owned records require `organization_id`; never trust a tenant identifier supplied by the client without resolving membership server-side.
- Use a transactional outbox for reliable domain events and PostgreSQL-backed background processing initially.
- Keep object storage private; use signed URLs and store metadata/ownership in PostgreSQL.
- Put AI access behind a provider-independent gateway with structured outputs, audit metadata, quotas, safety policies, and explicit human approval where clinical content is affected.
- Preserve portability through OIDC/JWT, PostgreSQL, containers, standard object-storage interfaces, and infrastructure-as-code where it adds value.

## Prototype boundary

The existing root `app/` is a high-fidelity prototype and may use hardcoded state. Do not mistake it for production domain logic.

When production implementation begins:

- Preserve the deployed prototype as a UX reference.
- Prefer extracting reusable tokens and presentational components over layering backend behavior into one large `page.tsx`.
- Introduce real features as vertical slices with API, authorization, persistence, tests, telemetry, and user-visible behavior together.
- Never place real patient data, secrets, API keys, or production identifiers in fixtures or screenshots.

## Quality expectations

- Run `npm run check:local` for prototype changes.
- Production code requires unit and integration tests at domain boundaries.
- Authorization and tenant-isolation tests are release blockers.
- Database migrations must be forward-tested and restoration procedures exercised before real health data is accepted.
- AI features require scenario evaluations, unsafe-response tests, traceability, and cost measurement before release.
- Keep decision records short and add an ADR when changing a major stack or tenancy decision.

## Working style

- Challenge assumptions with evidence.
- Prefer small, demonstrable increments deployed early for co-founder and pilot feedback.
- Preserve user changes and avoid broad rewrites without a clear migration path.
- Explain trade-offs in product terms, not only technical terms.
