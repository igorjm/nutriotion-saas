# ADR-0005: Zero-cost temporary staging

- Status: Accepted for Sprint 0
- Date: 2026-08-25

## Context

The founders want a $0 development and staging baseline until pilot evidence justifies paid infrastructure. Vercel Hobby is not appropriate for a commercial SaaS, Fly.io has no general free tier, and the long-term production topology still requires backups, uptime, and Brazil-region review.

## Decision

Use local Supabase for identity/database development. Use one Supabase Free project in São Paulo for fictional staging data and two Render Free web services in Virginia for the Next.js application and Spring API. GitHub Actions remains the merge gate; Render deploys only after checks pass.

The existing Sites deployment remains the unchanged prototype and is not a production application target.

## Consequences

- Render services sleep when idle and may take about one minute to wake.
- The two services share the workspace's free instance-hour allowance.
- Staging accepts no real personal, patient, or clinical data and has no uptime promise.
- Supabase Free backup, SMTP, MFA, and inactivity constraints are release blockers for real pilots.
- Before pilots, revisit the paid Vercel/Fly/Supabase topology and record the decision rather than silently promoting free staging to production.
