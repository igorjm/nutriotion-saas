# Local production development

## Prerequisites

- Java 25
- Node.js 22.19 or newer in the Node 22 line
- Docker with Compose

## Start

1. Start PostgreSQL with `docker compose -f infra/local/compose.yaml up -d postgres`.
2. Start the API with `SPRING_PROFILES_ACTIVE=dev services/api/mvnw -f services/api/pom.xml spring-boot:run`.
3. Copy `apps/web/.env.example` to `apps/web/.env.local` and start the web app from `apps/web`.

The development API recognizes `X-Dev-Subject` only under the `dev` or `test` profile. Never run those profiles in a shared or production environment.

## Verification

- API readiness: `http://localhost:8080/actuator/health/readiness`
- Runtime OpenAPI: `http://localhost:8080/v3/api-docs`
- Production web: `http://localhost:3000`
- Fictional development patient: `40000000-0000-4000-8000-000000000001`

Run `scripts/check-production.sh` before opening a pull request.
