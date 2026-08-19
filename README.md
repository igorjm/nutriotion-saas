# Brazil-first Nutrition SaaS

This repository is the Codex handoff for a SaaS platform connecting nutritionists and patients, initially in Brazil. It contains the complete interactive prototype, the product/market/architecture strategy, and the context required to continue into production development.

Current prototype: https://nutrition-practice-prototype.igorjmelo4.chatgpt.site

## What is included

- `app/` — interactive prototype covering nutritionist web, patient web, patient mobile concept, and acquisition landing page.
- `docs/product-strategy.md` — searchable version of the full market, product, architecture, AI, LGPD, cost, business-model, risk, and sprint analysis.
- `docs/product-strategy.pdf` and `docs/product-strategy.docx` — formatted strategy deliverables.
- `docs/prototype-brief.md` — original centralized UI/UX brief used to create the prototype.
- `docs/HANDOFF.md` — current state, constraints, recommended sequence, and prototype-to-production boundary.
- `AGENTS.md` — durable instructions for Codex and other coding agents working in this repository.

## Run the prototype locally

Prerequisites: Node.js `>=22.13.0` and npm.

```bash
npm install
npm run dev
```

Then open the URL printed by Vite. Use the top prototype switcher to move between the four product surfaces.

For a cross-platform validation suitable for macOS:

```bash
npm run check:local
```

The existing `npm run build` remains the Sites-compatible Linux build. It uses bounded GNU utilities supplied by the Sites environment.

## Important boundary

This is a high-fidelity interactive prototype, not the production application. Its data is hardcoded and it does not yet provide real authentication, authorization, persistence, tenancy, clinical audit trails, messaging, payments, or AI calls.

The prototype is a design specification and validation tool. Production development should incrementally replace its mock state with tested domain modules and APIs while preserving the validated workflows.

## Recommended production direction

- Nutritionist and patient web: Next.js App Router + TypeScript.
- Backend: Java 25 + Spring Boot 4.1 + Spring Modulith modular monolith.
- Data: PostgreSQL + Flyway; shared-schema multi-tenancy with `organization_id` and defense-in-depth RLS where applicable.
- API: REST + OpenAPI-generated TypeScript client.
- Async work: transactional outbox + PostgreSQL-backed jobs/JobRunr.
- MVP patient experience: responsive PWA; Expo/React Native after product validation.
- Initial managed infrastructure: Vercel + Fly.io GRU + Supabase São Paulo, behind portable adapters.

Read `AGENTS.md` and `docs/HANDOFF.md` before changing architecture or starting Sprint 0.
