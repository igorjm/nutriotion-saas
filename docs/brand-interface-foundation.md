# Vincelia brand and interface foundation

Status: working product direction, not a cleared trademark or permanent visual identity  
Updated: 2026-08-25  
Applies to: public site, nutritionist web, patient PWA, and future patient mobile app

## Working decision

Use **Vincelia** as the working masterbrand while the founders complete pronunciation, recall, domain, and trademark gates. The product should present one brand across every role rather than separate “Pro” and patient brands.

The brand idea is **vínculo em movimento**: the product helps the nutritionist and patient remain connected between consultations while the professional practice moves forward. This supports the complete product promise—clinical continuity, patient adherence, and ethical practice growth—without reducing the system to meal plans or AI.

This document is an implementation contract for the next design and frontend slices. It does not authorize a public launch, domain purchase, permanent logo, or trademark claim.

## 1. Name system

### Masterbrand

| Role | Decision |
| --- | --- |
| Written name | Vincelia |
| Pronunciation hypothesis | `vin-se-LI-a`; test without coaching before locking the name |
| Brand concept | Vínculo, continuity, and forward movement |
| Product category descriptor | Plataforma de cuidado nutricional |
| Brand promise | Cuidado que continua. |
| Commercial headline | Mais tempo para cuidar. Mais clareza para crescer. |

Do not accent, abbreviate, or create separate marks such as “Vincelia Pro” or “Vincelia Paciente” during the MVP. Role and context should explain the experience.

### Product architecture

| Layer | Customer-facing name | Rule |
| --- | --- | --- |
| Company and product | Vincelia | One masterbrand for Brazil and future markets where clearance permits |
| Nutritionist workspace | Vincelia | The authenticated role determines the professional shell |
| Patient PWA | Vincelia | The invitation and care relationship determine the patient shell |
| Practice-growth module | Crescimento | A module inside Vincelia, not a separate product |
| AI assistance | Assistência com IA | Prefer a transparent capability label over a branded AI persona |
| Future clinic edition | Para Clínicas | Use only after staff roles and multi-professional workflows exist |

### Retired fallback

Retire **Ritmera** from the shortlist. A 2026-08-25 collision screen found an active exact-name calculator platform at [ritmera.ru](https://ritmera.ru/) and [existing exact-name trademark activity](https://www.trademarkelite.com/uk/trademark/trademark-detail/UK00916663346/Ritmera) associated with Daikin outside Brazil. The different category or jurisdiction does not automatically block use, but the collision removes the reason to keep it as a low-risk fallback.

If Vincelia fails founder testing or legal screening, open a small replacement naming sprint. Do not revive Ritmera by default.

## 2. Positioning and verbal identity

### Positioning statement

For independent Brazilian clinical nutritionists who are considering leaving an incumbent platform, Vincelia connects practice management, patient follow-through, and ethical growth in one calm workflow. Unlike another diet calculator or an autonomous AI planner, it helps the professional make, approve, communicate, and follow up on decisions while remaining visibly in control.

### Message hierarchy

1. **Outcome:** more time to care and more clarity to grow.
2. **Proof:** one continuous consultation → plan → publication → adherence loop.
3. **Patient value:** simple daily guidance, approved substitutions, and lightweight follow-up.
4. **Switching reassurance:** assisted migration, validation, and exportability.
5. **Trust:** professional approval, auditability, privacy, and no silent clinical changes.

### Voice

- Calm, direct, warm, and specific.
- Speak to the nutritionist as a capable professional, not as someone being replaced by software.
- Speak to the patient with support and autonomy, never judgment or surveillance.
- Explain AI actions as drafts or suggestions that require review.
- Prefer `plano`, `acompanhamento`, `registro`, `revisar`, and `publicar` over inflated technology language.
- Avoid miracle claims, guaranteed outcomes, fear-based adherence copy, “autopilot,” and sparkle-first AI language.

Examples:

| Context | Preferred pt-BR copy | Avoid |
| --- | --- | --- |
| AI draft | Sugestão preparada para sua revisão | Plano criado automaticamente |
| Patient tracking | Como foi essa refeição? | Você falhou em seguir o plano |
| Plan publication | Revisar e publicar | Deixar a IA decidir |
| Growth | Planeje sua presença profissional com responsabilidade | Viralize e lote sua agenda |

## 3. Visual direction

### Character

**Calm professional competence + human connection + forward movement.**

The system should feel more human than enterprise clinical software and more trustworthy than a lifestyle or influencer app. It may be warm, but it must remain precise enough for clinical records and operational decisions.

### Mark concept

Explore a compact symbol made from **two paths forming a V**:

- one path represents professional judgment;
- the other represents the patient journey;
- their proximity expresses continuity without merging the two roles;
- a subtle upward or forward exit expresses progress;
- the silhouette must remain legible at favicon and mobile-app-icon sizes.

Avoid leaves, apples, plates, scales, measuring tapes, medical crosses, hearts used as the entire idea, and sparkle-led AI symbols. A permanent logo should not be commissioned until name clearance.

### Token foundation

The existing production tokens are the brand v0.1 baseline:

| Token | Value | Purpose |
| --- | --- | --- |
| `color.brandDark` | `#173F3B` | Navigation, strong headings, trust sections |
| `color.brandAction` | `#267565` | Primary actions, active states, links |
| `color.brandSoft` | `#DFEEE9` | Selected surfaces and calm emphasis |
| `color.humanAccent` | `#E99763` | Human warmth and selective growth emphasis |
| `color.canvas` | `#F7F5EF` | Warm application and marketing background |
| `color.ink` | `#17201E` | Primary text |
| `color.aiDraft` | `#7568A7` | Generated and draft states only |

Keep brand tokens separate from semantic tokens. Success, warning, error, information, AI-generated, and awaiting-approval states require text or icon labels and may not depend on color alone.

Use an 8px spacing base, 10–16px control/card radii, and restrained shadows. Keep Inter/system sans for the MVP foundation; typography experimentation should not block the first vertical slice.

## 4. System topology

```text
vincelia.com.br
├── /                         Public promise and early access
├── /seguranca               Trust, privacy, and AI-control explanation
├── /privacidade             LGPD-facing privacy information
├── /termos                  Product terms
└── /profissionais/{handle}  Future public profile; not MVP-critical

app.vincelia.com.br
├── /entrar                  Shared authentication entry
├── /convite/{token}         Patient invitation and consent entry
├── /profissional/*          Nutritionist workspace
└── /paciente/*              Patient PWA

api.vincelia.com.br
└── /api/v1/*                Authenticated versioned REST API
```

`vincelia.com.br` is the Brazil-first canonical domain candidate. On 2026-08-25, Registro.br RDAP returned no registration record for it, while Verisign RDAP returned no registration record for `usevincelia.com`; `vincelia.com` is registered. Recheck immediately before purchase because RDAP absence is not a reservation or trademark clearance.

## 5. Interface layout

### Public site

Use an editorial, evidence-led page with one primary conversion action:

```text
Header
Promise + product proof
Switcher pain recognition
Connected care workflow
Patient experience
Professional growth
AI and professional approval
Migration reassurance
Security and privacy
Founding offer
FAQ
Early-access form
Footer
```

The CTA is `Participar do acesso inicial`. Real staging screenshots should replace simulated product previews once the first vertical slice is usable.

### Nutritionist workspace

Desktop and large tablet:

- 240–256px persistent left rail.
- Contextual top bar with page title, search/command entry, notifications, and account/organization menu.
- Fluid main content on a 12-column grid with a practical maximum width near 1440px.
- 400–480px side sheets for contextual actions, including AI assistance, so the user does not lose the underlying patient or plan context.
- Dense tables only where comparison matters; the default experience should use attention queues and clear next actions.

Primary navigation:

| Destination | Purpose | MVP timing |
| --- | --- | --- |
| Hoje | Consultations, tasks, pending approvals, and patient attention | First shell |
| Pacientes | Organization-scoped patient list, invitations, and Patient 360 | Sprint 1–2 |
| Agenda | Appointments and preparation entry points | Sprint 2 |
| Planos | Drafts, review queue, versions, and publication | Sprint 3 |
| Crescimento | Content plan, leads, and follow-ups | Sprint 6 |
| Mensagens | Secure conversations and follow-up | After care relationship |

Do not make **Consultas** a permanent top-level destination. A consultation is a contextual workspace entered from Hoje, Agenda, or Patient 360. Do not make AI a top-level destination; assistance appears inside the workflow where the professional has the necessary context.

On narrow mobile screens, expose Hoje, Pacientes, Agenda, Planos, and Mais in bottom navigation. Professional mobile supports quick review and follow-up; complex plan building remains desktop-optimized for the MVP.

### Patient PWA

The patient shell is mobile-first and deliberately simpler:

| Bottom destination | Purpose |
| --- | --- |
| Hoje | Next meal, approved actions, check-in prompts, and messages requiring attention |
| Plano | Published plan, meal details, and approved substitutions |
| Registrar | Central quick action for meal/adherence registration |
| Progresso | Measurements, photos, trends, and weekly check-ins |
| Mensagens | Professional conversation and appointment follow-up |

Keep the nutritionist identity and practice visible in the header. Put profile, consent, privacy, notifications, and appointment details behind the account/context area rather than adding more bottom destinations. Every screen should have one dominant action and use patient-friendly language.

### Responsive ownership

| Breakpoint intent | Layout behavior |
| --- | --- |
| Wide desktop | Persistent professional rail, contextual panels, multi-column workspaces |
| Tablet | Compact rail, stacked secondary panels, full professional capability where practical |
| Mobile professional | Bottom navigation, review-oriented actions, no dense three-column builders |
| Mobile patient | Native-feeling PWA shell with bottom navigation and touch-first controls |

## 6. Shared interaction rules

- Show loading, empty, error, unauthorized, and success states as first-class components.
- Clinical publication always displays draft, reviewed, and published state explicitly.
- AI-generated content carries source/context metadata and a visible `Gerado com IA` or `Sugestão com IA` label.
- Destructive or irreversible actions require clear object names and consequences.
- Patient context is never inferred from a client-supplied organization identifier.
- Notification badges communicate actionable work, not engagement pressure.
- The prototype remains the interaction reference; production screens are extracted as tested vertical slices, not copied as one large component.

## 7. Implementation map

When the founder name gate passes, implement in this order:

1. Add one typed brand configuration for name, descriptor, canonical URLs, support contacts, and metadata. Do not scatter string literals.
2. Keep framework-neutral color and spacing values in `packages/design-tokens`; add semantic state tokens only after accessibility checks.
3. Create separate `MarketingShell`, `ProfessionalShell`, and `PatientShell` components in `apps/web`.
4. Add a server-side role router after authentication; do not let the browser choose a trusted Organization or role.
5. Replace `[PRODUCT_NAME]` in production metadata, manifest, landing, and authenticated shell from the shared brand configuration.
6. Deliver the professional invitation and patient consent layouts as the first cross-role vertical slice.
7. Add later navigation destinations only when their API, authorization, persistence, tests, telemetry, and user-visible behavior exist together.

Do not rename Java packages, Maven artifacts, npm scopes, or API identifiers merely to reflect the marketing name. Those internal identifiers are intentionally generic and can remain stable through a future brand change.

## 8. Name release gate

Before replacing the public placeholder or buying permanent identity work:

- [ ] Blind-test Vincelia with the nutritionist co-founder and at least five external nutritionists.
- [ ] Ask participants to pronounce it, write it after hearing it once, describe its associations, and recall it after 24 hours.
- [ ] Require at least 80% correct first-hearing spelling and no repeated clinic, supplement, diet-product, or autonomous-AI association.
- [ ] Decide whether the natural pronunciation is acceptable; do not coach the result into passing.
- [ ] Run exact, radical, phonetic, and visually similar searches in the [INPI trademark database](https://busca.inpi.gov.br/pePI/jsp/marcas/Pesquisa_classe_basica.jsp).
- [ ] Have specialist counsel assess the relevant Nice classes, expected to include software/downloadable software and SaaS, with healthcare-service adjacency only where the planned use warrants it.
- [ ] Recheck WIPO/priority-market records, domains, app stores, and key social handles.
- [ ] Register the chosen Brazil-first domain and essential email identities on the same day as the final decision.
- [ ] Only then replace production placeholders and commission the permanent wordmark/symbol.

The INPI recommends searching both identical and similar marks, including phonetic similarity; a zero-result exact web search is therefore insufficient legal evidence.
