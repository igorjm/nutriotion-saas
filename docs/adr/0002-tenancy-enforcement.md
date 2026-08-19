# ADR-0002: Resolve tenancy from authenticated membership

- Status: Accepted
- Date: 2026-08-19

## Context

Health records use a shared PostgreSQL schema. A client-provided tenant identifier would create an unacceptable cross-practice exposure risk.

## Decision

Every professional belongs to an Organization. The API resolves the default active Membership from the validated token subject. Organization-owned queries receive this resolved context internally; public request DTOs do not choose an `organization_id`. Relationship checks remain mandatory in addition to role checks.

## Consequences

- Cross-tenant reads return the same not-found response as absent records.
- Negative tenant-isolation integration tests are release blockers.
- A future multi-organization selector must change the active membership through a dedicated audited flow.
- PostgreSQL RLS remains defense in depth for later direct storage paths, not a replacement for application authorization.
