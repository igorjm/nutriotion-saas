# Security policy

Security and privacy are release concerns, not post-launch enhancements. This project handles architecture intended for LGPD-sensitive health data, although real patient data is not accepted during Sprint 0.

## Reporting a vulnerability

Do not open a public issue for vulnerabilities, suspected data exposure, credentials, authentication bypasses, or tenant-isolation failures.

Use GitHub's private vulnerability reporting for this repository. Include:

- the affected component and version or commit;
- reproduction steps using fictional data;
- expected and observed behavior;
- potential tenant, patient, or operator impact;
- suggested mitigation, if known.

Do not access, retain, or share data that does not belong to you. Stop testing and report immediately if you encounter real personal or health information.

## Supported versions

| Version | Supported |
| --- | --- |
| Latest `main` | Yes, pre-release |
| Deployed prototype | UX reference only |
| Older commits or forks | No |

## Security expectations

- Organization context must be resolved from authenticated membership server-side.
- Cross-tenant access must return an indistinguishable denial and have a negative test.
- Restricted data must not enter logs, analytics, screenshots, fixtures, or outbox payloads.
- Production files must remain private and use short-lived signed access.
- Clinical AI output requires traceability and explicit professional approval.

The current threat model is maintained in [`docs/security/threat-model.md`](docs/security/threat-model.md).
