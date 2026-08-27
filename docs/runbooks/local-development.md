# Local production development

## Prerequisites

- Java 25
- Node.js 22.19 or newer in the Node 22 line
- Docker with Compose
- Supabase CLI 2.115.0 (the commands below use `npx` so a global install is optional)

## Start

1. Start local Auth and PostgreSQL with `npx --yes supabase@2.115.0 start`.
2. Run `scripts/bootstrap-local-auth.sh`. It creates or refreshes a login-capable, confirmed Auth user only in the loopback Supabase instance.
3. Copy `apps/web/.env.example` to `apps/web/.env.local` and replace the publishable-key placeholder with `PUBLISHABLE_KEY` from `npx --yes supabase@2.115.0 status -o env`.
4. Start the API with the `local-auth` profile to exercise the real JWT resource-server path.
5. Start the web app from `apps/web`.

Example local API database values:

```text
SPRING_PROFILES_ACTIVE=local-auth
DATABASE_URL=jdbc:postgresql://localhost:54322/postgres
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
```

On a machine without Java 25, run the API through Docker Desktop:

```bash
docker run --rm -p 8081:8081 \
  -v "$PWD:/workspace" \
  -v nutrition-saas-maven-cache:/root/.m2 \
  -w /workspace/services/api \
  -e SPRING_PROFILES_ACTIVE=local-auth \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:54322/postgres \
  -e OIDC_JWK_SET_URI=http://host.docker.internal:54321/auth/v1/.well-known/jwks.json \
  eclipse-temurin:25-jdk ./mvnw spring-boot:run
```

The separate JWK URL lets a Dockerized API fetch local signing keys through Docker Desktop while still validating the token's browser-visible `http://127.0.0.1:54321/auth/v1` issuer exactly. The profile restricts verification to the local Auth server's ES256 signing algorithm.

The `local-auth` profile validates real local Supabase JWTs and maps the Auth `sub` to fictional application data. The `dev` and `test` profiles retain the explicit `X-Dev-Subject` adapter for API-focused tests only. Never run `dev`, `test`, or `local-auth` in a shared or production environment.

Default localhost-only credentials:

```text
Email: mariana.local@example.invalid
Password: LocalOnly!2026
```

These credentials are deliberately public and fictional. The bootstrap script refuses non-loopback Supabase URLs and non-`.invalid` email addresses; never reuse the password anywhere else.

## Verification

- API readiness: `http://localhost:8080/actuator/health/readiness`
- Local-auth API readiness: `http://localhost:8081/actuator/health/readiness`
- Runtime OpenAPI: `http://localhost:8080/v3/api-docs`
- Local-auth runtime OpenAPI: `http://localhost:8081/v3/api-docs`
- Production web: `http://localhost:3000`
- Local Auth email inbox: `http://localhost:54324`
- Local Supabase Studio: `http://localhost:54323`
- Fictional development patient: `40000000-0000-4000-8000-000000000001`

Sign in at `http://localhost:3000/auth/login`, then confirm `/professional` shows Mariana Costa and Consultório Mariana Costa. This proves that the browser session, JWT signature/issuer/audience checks, and server-resolved Organization membership all agree.

### Exercise the Sprint 1 relationship flow

1. Open `http://localhost:3000/professional/patients` as Mariana.
2. Create an invitation using only an invented name and an address ending in `.invalid`.
3. Open the generated invitation in a private browser window so it does not reuse the professional session.
4. Create the patient account with a local-only password. If confirmation is requested, open Mailpit at `http://localhost:54324` and follow the local confirmation link.
5. Return to the invitation, review the versioned consent, and activate the relationship.
6. Sign back in as Mariana and confirm the patient appears as `Ativo` in the Organization-scoped list.

The raw invitation token is returned once to the professional web and only its SHA-256 hash is persisted. Do not paste invitation links into issues, commits, screenshots, or shared logs even when they contain fictional data.

Run `scripts/check-production.sh` before opening a pull request.
