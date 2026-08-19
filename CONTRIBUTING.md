# Contributing

Thank you for helping improve the nutrition platform. The project is currently founder-led and in Sprint 0, so contributions should stay narrow, evidence-based, and aligned with the MVP.

## Before starting

1. Read `AGENTS.md` and `docs/HANDOFF.md`.
2. Search existing issues and pull requests before opening new work.
3. Discuss major architecture, tenancy, security, or product-scope changes in an issue first.
4. Never add real patient data, health records, credentials, access tokens, or production identifiers.

## Development workflow

- Branch from an up-to-date `main` using a descriptive name.
- Prefer one product or engineering concern per pull request.
- Use concise Conventional Commit-style messages such as `feat(api): add patient invitation`.
- Keep visible product copy in pt-BR and technical documentation in English.
- Add an ADR when changing a major stack, tenancy, persistence, or deployment decision.

## Required verification

Run checks relevant to the files you changed:

```bash
npm run check:local
npm --prefix packages/api-client run check
npm --prefix apps/web run check
./services/api/mvnw -f services/api/pom.xml verify
```

API changes should include unit or integration tests at domain boundaries. Authorization and tenant-isolation tests are mandatory for any Organization-owned record. Migrations must be forward-tested against PostgreSQL.

## Pull requests

Describe:

- the user or operator outcome;
- why the change is needed now;
- security, privacy, tenancy, migration, and operational impact;
- validation performed;
- screenshots using fictional data when UI behavior changes.

Draft pull requests are welcome for early technical feedback. A pull request should be marked ready only when its checks pass and its scope is reviewable.

## Licensing

This is proprietary source-available software. Contributions are accepted only for use in this project under its existing proprietary terms unless a separate written agreement says otherwise.
