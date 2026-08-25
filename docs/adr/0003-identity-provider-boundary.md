# ADR-0003: Supabase identity behind an OIDC boundary

- Status: Accepted
- Date: 2026-08-25

## Context

The product needs verified identities, PKCE, professional MFA, invitation-bound patient onboarding, recovery, and exportability. Building and operating identity would add high-risk work without differentiating the product.

## Decision

Use Supabase Auth for identity and session lifecycle. The Next.js application uses `@supabase/ssr`, PKCE, and cookie-backed sessions. The API remains an OAuth2 Resource Server and validates issuer, signature, expiry, and the `authenticated` audience. Provider subjects map to local `app_user` rows; roles, Organizations, and care relationships never rely on user-editable provider metadata.

Clinical professional routes require the Supabase `aal2` assurance claim. TOTP enrollment and recovery are implemented as explicit product flows before real clinical data is accepted. A development authentication filter exists exclusively in `dev` and `test` Spring profiles.

## Consequences

- Identity can be changed later without rewriting the authorization domain.
- Production startup requires an issuer URI and never enables development headers.
- Supabase secret/service-role keys are not used by the browser or required by the API.
- MFA and account recovery remain product flows rather than support database edits.
