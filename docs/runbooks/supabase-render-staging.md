# Supabase and Render staging runbook

This runbook creates a fictional-data-only Sprint 0 environment. It is not approved for real patient, clinical, or pilot data.

## Provider resources

1. Create a dedicated Supabase Free project in `sa-east-1` (São Paulo). Do not reuse an unrelated database.
2. Keep asymmetric JWT signing enabled and note the project URL, publishable key, database pooler host, database username, and database password.
3. Connect the GitHub repository to Render and create a Blueprint from the root `render.yaml`.
4. Keep both Render services on the Free instance type. Do not attach a Render database; Supabase owns staging PostgreSQL.

Provider dashboards are the only place to enter secrets. Never put a database password, refresh token, JWT private key, Supabase secret/service-role key, or provider token in GitHub issues, commits, logs, screenshots, or chat.

## Render web variables

| Variable | Value source | Secret |
| --- | --- | --- |
| `API_BASE_URL` | Render API public `https://...onrender.com` URL | No |
| `NEXT_PUBLIC_APP_NAME` | Selected product name | No |
| `NEXT_PUBLIC_APP_URL` | Render web public `https://...onrender.com` URL | No |
| `NEXT_PUBLIC_SUPABASE_URL` | Supabase project URL | No |
| `NEXT_PUBLIC_SUPABASE_PUBLISHABLE_KEY` | Supabase publishable key | Designed for browser use |

Never substitute a Supabase secret or legacy `service_role` key for the publishable key.

## Render API variables

| Variable | Value source | Secret |
| --- | --- | --- |
| `DATABASE_URL` | JDBC URL using the Supabase pooler with TLS | Yes |
| `DATABASE_USERNAME` | Supabase pooler username (`postgres.<project-ref>`) | Yes |
| `DATABASE_PASSWORD` | Supabase database password | Yes |
| `DATABASE_POOL_SIZE` | `3` for the 512 MB staging service | No |
| `OIDC_ISSUER_URI` | `<project-url>/auth/v1` | No |
| `OIDC_AUDIENCE` | `authenticated` | No |
| `WEB_ALLOWED_ORIGINS` | Exact Render web origin, without a trailing slash | No |
| `SPRING_PROFILES_ACTIVE` | `production` | No |

Use the Supabase transaction pooler JDBC form below and replace placeholders in the Render dashboard:

```text
jdbc:postgresql://<pooler-host>:6543/postgres?sslmode=require&prepareThreshold=0
```

Flyway runs forward migrations when the API starts. Do not run multiple manual migration jobs against staging at the same time.

## Supabase Auth settings

After Render assigns the web URL:

1. Set Auth Site URL to the exact Render web origin.
2. Add `<web-origin>/auth/callback` to allowed redirect URLs.
3. Keep anonymous sign-in and phone sign-in disabled.
4. Require email confirmation. Before external pilots, configure custom SMTP and verify delivery, bounce, and recovery behavior.
5. Use only `app_metadata` for provider-side authorization metadata if a future need is proven. Current Organization and role authorization remains in Spring/PostgreSQL.
6. Keep access-token expiry short. Do not lengthen it to hide refresh or deployment bugs.

The API rejects clinical patient routes without an `aal2` token. The free staging shell can validate ordinary login with fictional accounts; real clinical access remains blocked until the TOTP enrollment/recovery flow and an eligible provider plan are verified.

## Fictional founder account mapping

After a founder confirms a staging email, obtain its Auth user UUID from the Supabase Auth user list. Insert a matching `app_user`, one fictional Organization, and an active default `OWNER` membership through a reviewed SQL change. The value stored in `app_user.external_subject` is the Auth UUID (`sub`). Do not copy roles or organization identifiers from `raw_user_meta_data`.

Use `.invalid` addresses in automated tests. A real founder login address is Confidential and must not appear in migrations or repository fixtures.

## Verification

1. Open `/auth/login`, sign in, and confirm the callback returns to `/professional`.
2. Confirm `/api/v1/me/context` succeeds only after the local membership mapping exists.
3. Confirm a missing or revoked membership receives `403`.
4. Confirm `/api/v1/patients/<fictional-id>` receives `403` at `aal1` and cannot cross Organizations at `aal2`.
5. Confirm both Render health checks are green after a cold start.
6. Search web/API logs for the fictional canary values and confirm no token, password, email, or submitted content was logged.

## Zero-cost limitations and promotion gate

- Free Render services sleep after inactivity and are unsuitable for production uptime.
- Free instance usage is shared across the workspace; quota exhaustion suspends services when no payment method is present.
- Do not use artificial keep-alive traffic to evade the free-service policy.
- A free Supabase project does not meet the backup, recovery, email, MFA, and operational gates for real health data by assumption alone.

Before any real pilot data, approve a paid-production ADR, configure custom SMTP and MFA recovery, run a restore rehearsal, verify daily backups, and rerun tenant-isolation and logging controls.
