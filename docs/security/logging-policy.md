# Logging and telemetry policy

Logs may contain route templates, HTTP status, duration, trace ID, deployment version, job type, pseudonymous internal IDs, and exception class.

Logs and analytics must not contain names, email addresses, phone numbers, tokens, request bodies, clinical text, plan content, prompts, model outputs, photos, signed URLs, or raw database queries with values.

Errors returned to clients use stable problem types and neutral descriptions. Cross-tenant denial must not reveal whether a patient exists elsewhere. Session replay is disabled on authenticated clinical routes unless a later privacy review approves a comprehensively masked configuration.
