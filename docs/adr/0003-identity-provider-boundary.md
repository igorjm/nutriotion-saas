# ADR-0003: Provider-adapted OIDC identity

- Status: Accepted with provider decision pending
- Date: 2026-08-19

## Context

The product needs verified identities, PKCE, professional MFA, invitation-bound patient onboarding, recovery, and exportability. Provider availability, Brazilian region support, and final pricing still need confirmation.

## Decision

The API is an OAuth2 Resource Server and trusts a configured OIDC issuer. Provider subjects map to local `app_user` rows; roles and Organizations never live only in provider claims. A development authentication filter exists exclusively in `dev` and `test` Spring profiles.

## Consequences

- Supabase Auth remains the recommended first provider but can be changed without rewriting authorization.
- Production startup requires an issuer URI and never enables development headers.
- MFA and account recovery remain product flows for Sprint 1 rather than support database edits.
