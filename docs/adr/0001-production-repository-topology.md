# ADR-0001: Production repository topology

- Status: Accepted
- Date: 2026-08-19

## Context

The repository begins as a deployable Sites prototype at the root. Production needs a Next.js web client, a Java modular monolith, and a shared OpenAPI boundary without risking the validated prototype or adding monorepo orchestration overhead.

## Decision

Keep the prototype at the root during the migration. Add production code under `apps/web`, `services/api`, `contracts`, and `packages`. Use independent npm projects plus one Maven project. Do not add Nx or Turborepo during the MVP.

## Consequences

- Existing prototype deployment and checks remain recognizable.
- Production deployments can select `apps/web` and `services/api` as their roots.
- There is temporary dependency duplication until the prototype is retired.
- Moving the prototype to `apps/prototype` requires a separate verified deployment change.
