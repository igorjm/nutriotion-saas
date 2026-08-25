# Sprint 0 threat model

## Scope

Professional identity, Organization membership, patient relationship lookup, early-access capture, audit events, outbox records, browser-to-API traffic, and the initial PostgreSQL deployment.

## Trust boundaries

1. Public browser to Next.js public routes.
2. Authenticated browser to Next.js professional routes.
3. Next.js server to the REST API.
4. REST API to OIDC issuer, PostgreSQL, and later object storage/email providers.
5. Operator/support access to staging and production.

## Priority threats and controls

| Threat | Current control | Required before beta |
| --- | --- | --- |
| Cross-tenant IDOR/BOLA | Server-resolved Membership; relationship query; indistinguishable 404; negative integration test | Authorization matrix and independent review |
| Forged tenant identifier | No organization selector in patient request DTOs | Static rule/check as endpoint count grows |
| JWT theft or misuse | Supabase PKCE/cookie session; signature, issuer, expiry, and audience validation; `aal2` required on clinical patient routes | Session/device view, revocation, TOTP enrollment and recovery flow |
| Development-auth escape | Header filter compiled but activated only by `dev`/`test` profiles; Render fixes `production` profile | Deployment assertion rejecting dev profile |
| Invitation takeover | Not implemented | Opaque one-time token, expiry, recipient binding, attempt limits, audit |
| Public lead spam | Required fields and idempotent email update | Edge/IP rate limit, abuse telemetry, optional challenge after evidence |
| Sensitive logs | Trace ID only; error details exclude submitted values | Automated log canary and Sentry scrubbing test |
| Audit tampering | PostgreSQL trigger blocks update/delete | Separate DB permissions, export/monitoring, retention decision |
| Outbox data leakage | Event contains only opaque lead ID | Payload allowlist and worker logging policy |
| Object URL leakage | Files not implemented | Private bucket, ownership metadata, short signed URLs, negative tests |
| Mass export | Not implemented | Step-up MFA, job authorization, download expiry, audit and alerting |
| Malicious upload | Not implemented | Type/size/signature checks, quarantine and malware scan |
| Support abuse | No support role implemented | Disabled-by-default break-glass with reason, expiry and owner visibility |

## Data classification

- Restricted: health history, consultations, plans, measurements, photos, patient messages.
- Confidential: patient identity/contact data, professional account data, consent evidence.
- Internal: pseudonymous product events, operational metrics, feature flags.
- Public: published landing-page content.

No Restricted data is permitted in development, automated tests, logs, analytics, screenshots, or the Sprint 0 early-access table.
