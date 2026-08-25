# Local production development

## Prerequisites

- Java 25
- Node.js 22.19 or newer in the Node 22 line
- Docker with Compose
- Supabase CLI 2.115.0 (the commands below use `npx` so a global install is optional)

## Start

1. Start local Auth and PostgreSQL with `npx --yes supabase@2.115.0 start`.
2. Read the local URL and publishable key with `npx --yes supabase@2.115.0 status -o env` and copy the non-secret browser values into `apps/web/.env.local` using `apps/web/.env.example` as the contract.
3. Start the API against the local Supabase database. The development profile still uses the explicit fictional `X-Dev-Subject` adapter; production-style JWT verification is exercised by automated security tests and staging.
4. Start the web app from `apps/web`.

Example local API database values:

```text
SPRING_PROFILES_ACTIVE=dev
DATABASE_URL=jdbc:postgresql://localhost:54322/postgres
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
```

The development API recognizes `X-Dev-Subject` only under the `dev` or `test` profile. Never run those profiles in a shared or production environment.

## Verification

- API readiness: `http://localhost:8080/actuator/health/readiness`
- Runtime OpenAPI: `http://localhost:8080/v3/api-docs`
- Production web: `http://localhost:3000`
- Local Auth email inbox: `http://localhost:54324`
- Fictional development patient: `40000000-0000-4000-8000-000000000001`

Run `scripts/check-production.sh` before opening a pull request.
