# ADR-0004: Versioned REST and generated TypeScript client

- Status: Accepted
- Date: 2026-08-19

## Context

The professional web, patient PWA, and future mobile client need a stable API boundary.

## Decision

Use `/api/v1` REST endpoints. Keep the reviewed OpenAPI 3.1 contract in `contracts/openapi/openapi.yaml` and generate TypeScript path/schema types into `packages/api-client`. Runtime Spring documentation is tested for coverage of the contracted walking-skeleton operations.

## Consequences

- CI fails if regenerated TypeScript artifacts differ.
- Breaking changes require an explicit API-version decision.
- Exact runtime-to-contract semantic diffing should be added before the contract becomes large.
