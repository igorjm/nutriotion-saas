# Master Prototype Brief — Brazil-First Nutrition Practice SaaS

## How to use this specification

Use this document as persistent project context in Google Stitch or another UI/prototyping agent. Do not attempt to generate the entire product in one uncontrolled pass.

1. Import or paste **Part 1 — Master product and design context** first.
2. Ask the tool to create and retain a `DESIGN.md` from that context.
3. Generate the experiences sequentially:
   - Part 2 — Nutritionist web application
   - Part 3 — Patient responsive web application
   - Part 4 — Patient mobile application
   - Part 5 — Public acquisition landing page
4. After each part, verify navigation, component consistency, terminology, responsive behavior, accessibility, and the explicit user journeys.
5. Keep every screen in the same project and use the same design system. Do not redesign the visual language between surfaces.

The instructions are written in English for the design agent. All visible interface copy must be natural Brazilian Portuguese (`pt-BR`).

---

# Part 1 — Master product and design context

## Role and objective

Act as a senior product designer, UX architect, design-system specialist, and healthcare SaaS designer.

Create a coherent high-fidelity, interactive prototype for a Brazil-first SaaS platform that connects nutritionists and their patients. The platform is both:

1. A complete professional workspace for nutritionists to operate and grow their practice.
2. A simple, reassuring patient experience for understanding the nutrition plan, following it, communicating, and seeing progress.

This is not merely a meal-plan generator, calorie counter, weight-loss application, generic clinic CRM, or AI chatbot. It should support the complete nutrition care cycle:

**Acquire → onboard → assess → consult → plan → publish → follow → communicate → evaluate progress → retain → grow the practice.**

The first paying customers are independent Brazilian clinical nutritionists switching from products such as WebDiet, Dietbox, or Nutrium. They already have patients and care about fast migration, clinical reliability, ease of use, patient engagement, and professional differentiation.

Use **Vincelia** as the centrally configured working identity, with the promise **“Cuidado que continua.”** The name and visual mark remain provisional until the release gate in `docs/brand-interface-foundation.md` is complete; do not treat their implementation as trademark clearance.

## Primary personas

### Nutritionist

An independent Brazilian nutritionist with approximately 20–150 active patients. The professional already uses another nutrition platform but is frustrated by slow workflows, interface complexity, repetitive plan creation, weak adherence visibility, fragmented communication, and difficulty attracting new clients.

The nutritionist needs to feel:

- In control of clinical decisions.
- Faster without being rushed.
- Professionally credible.
- Supported by AI without being replaced by it.
- Able to understand each patient before and after consultations.
- Better equipped to organize and grow the practice ethically.

### Patient

An invited patient following a plan created and approved by a nutritionist. The patient may have goals related to clinical health, food education, sports performance, pregnancy, hypertrophy, body composition, or general wellbeing—not only weight loss.

The patient needs to feel:

- Guided rather than judged.
- Able to understand what to do today.
- Able to make safe substitutions without abandoning the plan.
- Close to the nutritionist while respecting communication boundaries.
- Motivated by meaningful, non-punitive progress.

### Future clinic organization

The architecture may later support clinic owners, assistants, and multiple professionals. Do not make clinic administration the primary prototype. Represent future team functionality only inside settings or plan comparison where necessary.

## Product principles

- **Professional-first, patient-centered.** The nutritionist owns clinical decisions; the patient experience should still be excellent.
- **One clear next action.** Every screen should make the most important task obvious.
- **Fast workflows.** Reduce repetitive input, unnecessary modals, tab switching, and duplicate data entry.
- **Progressive disclosure.** Professional screens may be information-dense, but should reveal advanced controls contextually.
- **Human-in-the-loop AI.** AI creates drafts, summaries, explanations, and suggestions. It must not silently publish plans, change clinical records, diagnose, or contact patients.
- **Visible state and versioning.** Draft, under review, approved, published, superseded, and archived states must be clear.
- **Calm healthcare experience.** Avoid fear, shame, guilt, “bad food” language, and aggressive red/error states for ordinary deviations.
- **Privacy by design.** Sensitive information should never appear casually in notifications or marketing surfaces.
- **Brazilian context.** Use pt-BR language, Brazilian date/time formats, kilograms, centimeters, household measures, familiar foods, and currency in BRL.
- **Mobile-first patient behavior.** The patient web application must already anticipate the future mobile application and shared backend.
- **Growth with professional ethics.** Marketing tools support planning and drafting, but never promise results, auto-post content, fabricate evidence, or encourage prohibited before/after claims.

## Prototype scope strategy

Design the complete product vision, but indicate depth:

- **MVP core:** detailed, clickable, and production-plausible.
- **Post-MVP:** represented through coherent concept screens or secondary states.
- **Advanced:** visible only when it helps communicate future product direction; do not let it dominate core journeys.

Do not fill the navigation with disabled “coming soon” items. Prefer a focused interface and place future concepts in a clearly labeled exploration area when needed.

## Shared design language

Create a sophisticated, calm, modern health SaaS identity that feels Brazilian but internationally credible.

### Temporary visual tokens

These are provisional design tokens, not a permanent brand commitment:

- Brand dark: `#173F3B`
- Primary action: `#267565`
- Warm human accent: `#E99763`
- Canvas/background: `#F7F5EF`
- Primary text: `#17201E`
- Surface: `#FFFFFF`
- Borders: cool neutral gray with adequate contrast
- AI draft accent: muted violet used only for generated/draft states

Validate text and interactive-state contrast against WCAG 2.2 AA. Do not rely on color alone for status.

### Style characteristics

- Warm professional competence.
- Human connection without childish illustrations.
- Structured and fast, without looking like an ERP.
- Generous whitespace on patient and landing surfaces.
- Compact but breathable information density in the nutritionist workspace.
- Subtle depth through borders and restrained shadows.
- Rounded corners should be moderate, not bubble-like.
- Use food imagery selectively and realistically; the product should not look like a recipe blog.

### Avoid

- Leaves, apples, measuring tapes, bathroom scales, plates, medical crosses, or sparkles as the central logo concept.
- Generic bright-green wellness templates.
- Excessive gradients, glassmorphism, floating blobs, neon colors, or purple-everywhere “AI” styling.
- Emoji as permanent navigation icons.
- Fake medical claims, dramatic weight-loss imagery, or fabricated testimonials.
- Overuse of charts when a direct status or action is clearer.
- Dense tables on patient/mobile screens.

### Typography and iconography

- Use a highly legible contemporary sans-serif such as Inter or a comparable variable font.
- Use clear hierarchy, tabular numerals where useful, and comfortable pt-BR line lengths.
- Use a consistent outlined icon set such as Lucide.
- Pair icons with labels in primary navigation.

### Layout system

- Desktop professional workspace: optimized around 1440px, functional from 1280px, with a collapsible left navigation rail and contextual right panel or drawer.
- Patient responsive web: optimized around 390px and 768px but expands gracefully on desktop.
- Mobile: native-feeling iOS and Android layouts, with platform-safe areas and accessible touch targets.
- Use an 8px spacing system and a 12-column desktop grid where appropriate.

### Core shared components

Create reusable variants for:

- Buttons, icon buttons, text links, segmented controls.
- Inputs, search, comboboxes, date/time inputs, autocomplete, tags.
- Cards, metric cards, attention cards, empty states.
- Tables with filters, sorting, pagination, and bulk-selection restraint.
- Tabs, breadcrumbs, steppers, timelines, accordions.
- Drawers, dialogs, tooltips, confirmation states.
- Status badges with icon + text.
- Toasts and inline success/error messaging.
- Charts with readable labels and accessible summaries.
- File uploader and media viewer.
- AI suggestion card with source/context, uncertainty, approve, edit, reject, and audit state.
- Loading skeleton, offline, empty, permission-denied, validation-error, and recovery states.

## Shared domain terminology in pt-BR

Prefer professional Brazilian terminology:

- Paciente
- Consulta
- Avaliação
- Histórico clínico
- Anamnese
- Plano alimentar
- Refeição
- Alimento
- Receita
- Porção
- Medida caseira
- Substituição
- Medidas e evolução
- Objetivo
- Acompanhamento
- Adesão or “como foi a semana” depending on patient tone
- Rascunho
- Em revisão
- Aprovado
- Publicado
- Arquivado
- Conteúdo gerado por IA
- Requer aprovação profissional

Use “nutricionista” rather than generic “especialista” when the role is known. Avoid diagnosing language in the patient assistant.

## Realistic prototype data

Use fictional but realistic Brazilian names and foods. Do not use Lorem Ipsum.

Suggested fictional nutritionist:

- Mariana Costa
- “Nutricionista” without inventing a real CRN number
- Practice focus: clinical nutrition and sports performance

Suggested fictional patients:

- Camila Ribeiro
- Lucas Martins
- Renata Alves
- João Pedro Lima
- Beatriz Souza

Suggested foods and meals:

- Arroz integral, feijão carioca, frango grelhado, patinho moído
- Banana, mamão, aveia, iogurte natural
- Pão francês, queijo minas, ovos mexidos
- Mandioca, batata-doce, cuscuz, tapioca
- Salada, legumes assados, azeite

Show household measures and grams together when useful. Show data provenance such as TACO/TBCA without presenting either source as infallible.

## Shared AI interaction policy

AI is embedded contextually, not a permanent omnipresent chatbot.

AI may:

- Summarize a patient history before a consultation.
- Transform notes into a structured draft.
- Draft a plan from nutritionist-defined constraints.
- Suggest equivalent substitutions.
- Identify adherence patterns and changes between consultations.
- Draft patient-friendly explanations.
- Suggest follow-up questions.
- Draft an ethical social-content calendar or post.
- Answer patient questions grounded in the approved plan and professional rules.

AI must:

- Visibly label generated content.
- Show whether professional approval is required.
- Allow editing, approving, rejecting, and reporting.
- Preserve the source/context used to generate the suggestion.
- Escalate clinical uncertainty and emergencies to the nutritionist or appropriate care.
- Never independently publish a plan, alter an approved plan, diagnose, prescribe, or promise outcomes.

## Shared security and trust cues

Represent security calmly and credibly:

- Consent and privacy choices during onboarding.
- MFA/security settings for professionals.
- Session/device management.
- Audit/history for clinical and AI-assisted changes.
- Private media and signed access behavior conceptually.
- Export/correction/deletion request surfaces.
- Explicit “only your nutritionist and authorized team can access this” explanations.

Do not claim certification or legal compliance that has not been independently verified. Use wording such as “designed with LGPD-oriented privacy controls” in marketing prototypes.

---

# Part 2 — Nutritionist web application prompt

## Generation instruction

Using the retained master context and design system, create the nutritionist’s desktop-first responsive web application. Generate the screens on one organized canvas, connect the principal interactions into playable flows, and reuse components rather than creating isolated visual concepts.

The workspace should feel faster and clearer than legacy nutritionist software. Optimize for keyboard use, predictable navigation, minimal duplicate entry, autosave, and clear version history.

## Nutritionist information architecture

Use a collapsible left navigation with these primary destinations:

1. **Hoje**
2. **Pacientes**
3. **Consultas**
4. **Planos**
5. **Agenda**
6. **Acompanhamento**
7. **Mensagens**
8. **Crescimento**
9. **Insights**
10. **Documentos**
11. **Configurações**

Use the top bar for global search/command access, quick creation, notifications, help, and account/profile. Place contextual AI actions inside the relevant workspace rather than making “AI” the first navigation item.

## Required screen group A — Authentication and switching onboarding

Create:

1. Sign-in with email/password and passwordless option.
2. Account creation with verified email.
3. Professional profile setup.
4. Practice configuration: focus areas, consultation duration, availability, communication boundaries.
5. Migration choice:
   - Import supported CSV.
   - Upload an archive/document export for assisted review.
   - Start manually.
6. Import mapping and preview with reversible validation.
7. Import result with successful, warning, and rejected rows.
8. First-run checklist leading to the first patient and first plan.

The migration flow must communicate that data is previewed before commit. Do not promise universal automatic migration.

## Required screen group B — Today dashboard

Create a high-value daily control center containing:

- Greeting and current date.
- Today’s appointments with intake/readiness status.
- “Prepare next consultation” card with an AI-generated pre-consultation brief.
- Tasks requiring professional action.
- Patients needing attention based on missed check-ins or repeated deviations.
- Recent patient questions.
- Draft plans awaiting review or publication.
- New leads and overdue follow-ups from the Growth module.
- Practice snapshot: active patients, consultations this week, response backlog, adherence trend.
- Quick actions: new patient, new consultation, create plan, schedule appointment, add lead.

Avoid turning every metric into a large card. Prioritize actionable work over vanity analytics.

## Required screen group C — Patient management

### Patient list

Create a fast table/list with:

- Search and saved filters.
- Patient status: invited, onboarding, active, paused, discharged/archived.
- Goal or care focus.
- Last consultation and next appointment.
- Last patient activity.
- Adherence/engagement signal with explanation, not a mysterious score.
- Attention flags.
- Responsible nutritionist for future clinic support.
- Row quick actions.

Include useful empty, loading, and no-results states.

### Create/invite patient

Create a short flow:

1. Basic identity and contact.
2. Care relationship and communication preferences.
3. Select intake/anamnesis form.
4. Optional appointment.
5. Review invitation and privacy wording.

### Patient 360 workspace

Create a patient workspace with a persistent summary and these tabs:

- Visão geral
- Histórico
- Avaliações
- Medidas e evolução
- Objetivos
- Planos
- Acompanhamento
- Mensagens
- Documentos
- Atividade

The overview should include:

- Patient identity and care focus.
- Next appointment and last consultation.
- Current approved plan.
- Recent measurements and progress.
- Recent check-ins and questions.
- Allergies, intolerances, restrictions, and relevant alerts.
- AI-generated summary clearly labeled as a draft/summary.
- Timeline of meaningful clinical and engagement events.

Make relationship-level access explicit so a clinic user cannot casually switch between unrelated patients.

## Required screen group D — Consultation workspace

Create a unified consultation screen that minimizes tab switching.

Include:

- Pre-consultation brief: changes since last visit, adherence pattern, measurements, unanswered questions, and suggested follow-up questions.
- Intake/anamnesis responses.
- Structured clinical sections with autosave.
- Free notes with keyboard shortcuts.
- Measurements and body-composition entry.
- Goals and agreed actions.
- Relevant documents/media.
- Prior consultation comparison.
- AI action: transform raw notes into a structured draft.
- Explicit review step before finalizing the clinical note.
- Immutable finalized note with amendment flow and reason.
- Actions after consultation: create/update plan, schedule follow-up, send explanation, assign check-in.

Prototype states:

- In progress.
- Autosaved.
- AI draft available.
- Finalized.
- Amendment requested.

## Required screen group E — Food database and fast plan builder

This is a central product workflow. Create a powerful but understandable split-view or multi-column plan builder.

### Food search and data

Support:

- TACO/TBCA source labels and provenance.
- Search by common Brazilian names and aliases.
- Branded/custom foods visibly separated from canonical reference foods.
- Grams, portions, and household measures.
- Macro and micronutrient details.
- Recipes and recipe portions.
- Favorites and professional foods.
- Confidence/source indication without overwhelming the interface.

### Plan builder

Include:

- Patient constraints and goals visible while editing.
- Day or routine structure.
- Meals and meal times.
- Drag/reorder or efficient keyboard movement.
- Add foods, recipes, instructions, and optional notes.
- Real-time nutritional totals.
- Templates and clone-from-prior-plan.
- Equivalent substitutions with professional-defined constraints.
- AI-generated draft from explicit professional parameters.
- Difference comparison between current and prior versions.
- Patient preview.
- Draft → review → publish flow.
- Version history and superseded plan state.
- PDF/print preview as a secondary output, not the primary patient experience.

Show that substitutions are approved options rather than unlimited AI improvisation.

### Required clickable plan flow

1. Open Camila Ribeiro.
2. Start from a prior plan/template.
3. Adjust breakfast and lunch.
4. Request substitution suggestions for one item.
5. Accept and edit one suggestion.
6. Review nutritional totals and patient constraints.
7. Preview patient experience.
8. Publish version 2.
9. Show audit/history confirmation.

## Required screen group F — Scheduling and appointments

Create:

- Day/week/month calendar.
- Appointment creation and rescheduling.
- Patient, type, duration, location/modality, and reminder settings.
- Intake status visible before the appointment.
- Upcoming appointment list for mobile-sized responsive state.
- Cancellation/no-show state.
- Calendar integration placeholder as post-MVP.

Do not design built-in video infrastructure as an MVP requirement. A remote appointment may contain an external meeting link.

## Required screen group G — Adherence and patient monitoring

Create an attention-oriented workspace, not a surveillance dashboard.

Include:

- Weekly check-in completion.
- Meal followed/changed/skipped events.
- Patient notes and photos.
- Repeated substitution patterns.
- Missed interactions.
- Patient questions awaiting response.
- Trend view by patient and cohort.
- Explainable attention flags with evidence.
- Assign follow-up task or send a message.
- AI-generated adherence summary with professional review.

Use neutral language such as “ponto de atenção” and “oportunidade de acompanhamento.” Avoid “failure,” “cheating,” or red punishment indicators.

## Required screen group H — Secure messaging and notifications

Create:

- Conversation list with unread and response-needed filters.
- Patient conversation with plan/meal context.
- Attachments and approved quick responses.
- Communication-hours notice.
- Escalation disclaimer for urgent or emergency situations.
- Message audit metadata without making the interface feel hostile.
- Notification preference management.

Separate clinical communication from acquisition leads.

## Required screen group I — Documents and reports

Create:

- Patient documents with privacy and type filters.
- Upload and preview.
- Report generation from approved data.
- Export center showing queued, processing, completed, and failed jobs.
- Patient data export/request concept.
- Branded plan/report templates as post-MVP.

## Required screen group J — Practice Growth workspace

This is an important differentiator. It must help nutritionists find and convert clients while respecting Brazilian professional ethics.

Create these connected areas:

### Weekly/monthly growth planner

- Calendar or board for themes, content, leads, partnerships, and follow-ups.
- Weekly objectives and realistic capacity.
- Reusable content themes.
- Tasks integrated with the Today dashboard.

### Content pipeline

Statuses:

- Ideia
- Rascunho
- Revisão ética
- Aprovado
- Publicado manualmente
- Reaproveitar

Capabilities:

- Capture content ideas from common patient questions.
- AI-assisted pt-BR post drafts.
- Carousel/reel/story outline variants.
- Educational-source/evidence note.
- Claims and ethics checklist.
- Professional approval.
- Export/copy for manual publishing.

Do not auto-publish in the initial product and do not fabricate patient stories, endorsements, or guaranteed outcomes.

### Lead inbox and pipeline

Create:

- New inquiry.
- Qualified.
- Consultation offered.
- Scheduled.
- Converted to patient.
- Not now/lost.

Include source attribution, follow-up task, notes, communication consent, and explicit conversion into a patient relationship. Marketing lead data must remain separated from clinical records until conversion and consent.

### Professional public profile

Create a preview/editor for a future indexable profile containing:

- Photo, introduction, focus areas, consultation modality, location, and CTA.
- Professional credentials without inflated claims.
- Lead form preview.
- Privacy and consent wording.

### Growth insights

Show useful early metrics:

- Qualified inquiries.
- Response time.
- Consultation booking conversion.
- Source/channel.
- Content production consistency.

Avoid follower-count vanity metrics as the main success measure.

### Required clickable growth flow

1. Open the monthly planner.
2. Select an educational theme.
3. Generate a weekly content outline.
4. Open one Instagram carousel draft.
5. Edit language and review ethics checklist.
6. Approve for manual export.
7. Open a related lead inquiry.
8. Schedule a follow-up task.
9. Convert the qualified lead into an invited patient.

## Required screen group K — Insights

Create a focused analytics area with:

- Active patients and care-loop completion.
- Patient activation after invitation.
- Check-in participation and engagement.
- Average time to respond.
- Plan publication cycle time.
- Consultation preparation time saved.
- Growth funnel.
- AI usage and acceptance/rejection.

Always allow drill-down or explanation. Avoid unsupported causality claims such as “AI improved patient health by X%.”

## Required screen group L — Settings, security, and commercial controls

Create settings for:

- Professional and practice profile.
- Availability and appointment types.
- Communication boundaries.
- Templates.
- Branding basics.
- Notification preferences.
- Security, MFA, devices, sessions.
- Privacy requests and retention controls.
- Integrations.
- Subscription and AI allowance.
- Future team/clinic roles.

## Nutritionist prototype acceptance criteria

The result is acceptable only if:

- A tester can complete the first patient → consultation → plan → publish flow without explanation.
- The plan builder looks production-plausible and faster than a legacy form-heavy tool.
- Clinical, patient-engagement, and marketing data are visually separated where appropriate.
- AI outputs are labeled and require approval in sensitive workflows.
- The Today screen prioritizes action, not decoration.
- The Growth workspace feels like part of the professional operating system, not a separate social-media toy.
- Navigation and terminology remain consistent across every screen.

---

# Part 3 — Patient responsive web application prompt

## Generation instruction

Using the same project and retained design system, create a responsive patient web/PWA experience. It must feel much simpler than the professional workspace. Design mobile-first and then show the desktop responsive expansion.

The patient should normally see what matters **today**, not the complexity of the nutritionist’s clinical system.

## Patient information architecture

Use five primary destinations:

1. **Hoje**
2. **Plano**
3. **Registrar**
4. **Progresso**
5. **Mensagens**

Appointments, notifications, AI help, consent, and profile/settings may be accessed contextually or through the account area.

## Required patient screen group A — Invitation and onboarding

Create:

1. Secure branded invitation showing the nutritionist’s identity.
2. Account setup and verified contact.
3. Consent and privacy explanation in plain pt-BR.
4. Notification preferences.
5. Guided intake/anamnesis with progress indicator.
6. Goals, food preferences, routine, allergies/intolerances, and restrictions.
7. Photo/measurement permission choices.
8. Completion screen with next appointment and what happens next.

Do not force optional marketing consent as a condition for care.

## Required patient screen group B — Today

Create a calm daily home containing:

- Personal greeting.
- Current meal or next meal.
- One dominant action: view meal, register how it went, or answer check-in.
- Simple day timeline.
- Nutritionist message or explanation.
- Reminder for upcoming appointment.
- Weekly progress/check-in card.
- Small motivational insight grounded in the plan, without generic AI advice.

Avoid calorie-ring obsession as the universal home experience.

## Required patient screen group C — Approved nutrition plan

Create:

- Day/routine view.
- Meal cards with time, purpose, foods, portions, and household measures.
- Meal detail with instructions and recipe when applicable.
- Approved substitutions.
- Nutritionist notes and educational explanations.
- Current plan version and publication date.
- Clear distinction between the approved plan and historical plans.
- Offline/weak-connection behavior concept.

### Substitution flow

1. Open breakfast.
2. Select “Preciso substituir”.
3. Choose reason or context.
4. Show only nutritionist-approved or constraint-safe alternatives.
5. Explain equivalent portions.
6. Confirm the choice.
7. Record it for the nutritionist without rewriting the approved plan.

If no approved option exists, offer “Perguntar à nutricionista” rather than inventing a substitution.

## Required patient screen group D — Quick tracking

Create a low-friction interaction allowing the patient to record:

- Segui como planejado.
- Fiz uma substituição.
- Não consegui seguir.
- Optional short note.
- Optional meal photo.
- Hunger/satiety or symptoms only when explicitly configured by the nutritionist.

The simple action should take approximately ten seconds. Do not require exhaustive calorie logging for every patient.

## Required patient screen group E — Weekly check-in

Create a conversational but structured check-in containing only professionally configured questions such as:

- How the week felt.
- Energy.
- Sleep.
- Training/movement.
- Main difficulty.
- What worked well.
- Measurement or weight when appropriate.
- Question for the next consultation.

Show save-and-return behavior and a reassuring completion state.

## Required patient screen group F — Progress

Create:

- Goal overview.
- Weight/measurement trends when clinically relevant.
- Progress photos with privacy control.
- Habit/check-in consistency.
- Personal achievements and nutritionist feedback.
- Comparison periods chosen by the patient/professional.
- Accessible text summary of trends.

Use non-judgmental copy. Weight should not dominate patients whose care goal is unrelated to weight.

## Required patient screen group G — Contextual AI assistance

Create a contextual assistant entered from the plan or meal, not an unrestricted general health chatbot.

Supported examples:

- “Por que esta refeição faz parte do meu plano?”
- “O que posso usar no lugar deste alimento?”
- “Como entender esta porção?”
- “Tenho estes ingredientes em casa. Qual opção aprovada funciona?”
- “Ajude-me a preparar uma pergunta para minha nutricionista.”

The assistant must:

- State that it is using the approved plan and professional-defined rules.
- Show when information came from the plan.
- Refuse diagnosis or autonomous plan changes.
- Escalate uncertainty to the nutritionist.
- Offer a direct “Perguntar à nutricionista” action.
- Label AI-generated responses.

## Required patient screen group H — Messages and appointments

Create:

- Secure conversation with the nutritionist.
- Plan/meal context attachment.
- Expected response-hours message.
- Urgent-care disclaimer.
- Appointment list/detail.
- Confirm, request reschedule, and cancellation states.
- Intake reminder.

## Required patient screen group I — Profile, consent, privacy, and notifications

Create:

- Personal data.
- Preferences and restrictions.
- Notification schedule.
- Connected nutritionist/practice.
- Consent history and optional permissions.
- Request data correction/export/deletion.
- Session/device security.
- Sign out.

## Required clickable patient journey

1. Open invitation from Mariana Costa.
2. Create account and review privacy/consent.
3. Complete a short intake.
4. Arrive on Today.
5. Open the current meal.
6. Request and confirm an approved substitution.
7. Register how the meal went.
8. View progress.
9. Ask a plan-grounded question.
10. Send a contextual follow-up to the nutritionist.
11. Review the next appointment.

## Patient web acceptance criteria

- The current plan is visible within one action after login.
- A simple meal check-in can be completed quickly with one hand on mobile.
- The patient never confuses AI output with the nutritionist’s approved plan.
- Deviations are recorded without shame-based language.
- The nutritionist’s identity and care relationship remain visible.
- Screens work convincingly at 390px, 768px, and desktop widths.

---

# Part 4 — Patient mobile application prompt

## Generation instruction

Using the same product, content model, and design tokens, create the future patient mobile application. It should share conceptual components and terminology with the patient PWA while feeling native rather than like a compressed website.

Show representative iOS screens and ensure the layout can translate to Android. Use native safe areas, bottom navigation, large touch targets, camera patterns, sheets, and push-notification behavior.

## Mobile navigation

Use bottom navigation:

1. **Hoje**
2. **Plano**
3. **Registrar**
4. **Progresso**
5. **Mensagens**

Use contextual top-bar actions for notifications, help, and account settings.

## Required mobile screens

1. Invitation deep-link handoff.
2. Sign-in/account setup.
3. Consent and notification onboarding.
4. Today home.
5. Daily plan timeline.
6. Meal detail.
7. Approved substitution bottom sheet.
8. Ten-second meal check-in.
9. Camera/photo capture and review.
10. Weekly check-in.
11. Progress overview.
12. Measurement entry.
13. Private progress photo gallery.
14. Plan-grounded AI question.
15. Message conversation.
16. Appointment detail.
17. Notification center.
18. Account/privacy/security settings.
19. Offline cached-plan state.
20. Sync recovered/conflict state.

## Mobile-specific behavior

- Support push reminders without exposing sensitive clinical information on the lock screen.
- Offer biometric unlock as a future security option.
- Cache the currently approved plan and essential meal information for offline viewing.
- Queue non-critical check-ins when offline and clearly show sync status.
- Use the camera for optional meal/progress photos with explicit context and privacy explanation.
- Keep generated suggestions and professional-approved content visually distinguishable.
- Use haptics/motion sparingly and respect reduced-motion settings.

## Required playable mobile flow

1. Tap a reminder for the next meal.
2. Open meal details.
3. Choose an approved substitution.
4. Confirm and complete a quick check-in.
5. Optionally attach a photo.
6. View the updated Today state.
7. Ask a plan-grounded question.
8. Escalate the question to the nutritionist.

## Mobile acceptance criteria

- The application feels native, not like the desktop application reduced to a phone.
- Core actions are reachable with one thumb.
- The primary patient loop works under weak connectivity.
- Notifications are useful but privacy-preserving.
- No dense data table appears on mobile.

---

# Part 5 — Public acquisition landing page prompt

## Generation instruction

Using the same provisional brand system, create a responsive pt-BR acquisition landing page for independent Brazilian nutritionists who are considering switching from another paid platform.

The page should not sell an enormous feature list. It should sell the outcome:

- Safer, supported migration.
- Faster professional workflows.
- Better patient experience and adherence visibility.
- AI assistance with professional control.
- Ethical tools to organize and grow the practice.

Primary conversion: apply for early access or request a guided demonstration.

## Required landing-page structure

### 1. Navigation

- Vincelia working wordmark and shared V-path mark.
- Como funciona.
- Para nutricionistas.
- Experiência do paciente.
- IA com controle.
- Crescimento.
- Segurança.
- Entrar.
- Primary CTA: “Quero participar do acesso antecipado”.

### 2. Hero

Working headline:

> **Menos tempo montando planos. Mais tempo cuidando — e crescendo.**

Supporting copy:

> Gestão, acompanhamento de pacientes e ferramentas para fortalecer sua presença profissional em um só lugar.

Primary CTA:

> Quero participar do acesso antecipado

Secondary CTA:

> Ver como funciona

Include a credible product preview showing the nutritionist Today workspace and patient Today experience. Avoid fabricated company logos, review counts, or customer numbers.

### 3. Switcher problem recognition

Describe recognizable problems:

- Hours rebuilding plans and searching foods.
- Patient information scattered across forms, PDFs, and messages.
- Difficulty seeing who needs attention.
- Patient applications that feel secondary.
- Social-media and acquisition work disconnected from the practice routine.

Use concise outcome-oriented copy, not attacks on named competitors.

### 4. Complete professional care loop

Visualize:

> Paciente → avaliação → consulta → plano → acompanhamento → evolução → retorno

Explain that the platform maintains continuity instead of treating the plan as the final deliverable.

### 5. Fast nutritionist workflow

Show an interactive or stepped product preview:

1. Prepare consultation.
2. Record assessment.
3. Build/reuse plan.
4. Review AI suggestions.
5. Publish.
6. Monitor follow-up.

### 6. Patient experience

Show mobile cards for:

- Today’s plan.
- Approved substitution.
- Quick check-in.
- Progress.
- Secure question to the nutritionist.

Copy should emphasize clarity, convenience, and the nutritionist’s continuing role.

### 7. AI with professional control

Explain contextual AI capabilities:

- Pre-consultation summaries.
- Structured note drafts.
- Plan and substitution suggestions.
- Adherence insights.
- Patient-friendly explanations.
- Ethical content drafts.

State prominently:

> A IA sugere. A decisão profissional continua sendo sua.

Show draft, review, approve, and publish states.

### 8. Growth workspace

Position it as an organized professional-growth workflow:

- Weekly/monthly content planning.
- Common-question idea bank.
- Ethical AI-assisted content drafts.
- Lead inbox and follow-up tasks.
- Public professional profile.
- Conversion visibility.

Do not promise followers, revenue, or patient results.

### 9. Migration and onboarding

Explain:

- Guided import preview.
- Validation before committing records.
- Founding-cohort support.
- Ability to test with real workflows before fully switching.

Do not claim one-click migration from every competitor.

### 10. Security and privacy

Explain in plain language:

- Health data deserves additional care.
- Controlled professional/patient access.
- Consent and audit history.
- Encryption and private files concept.
- Export and privacy-request support.
- LGPD-oriented product design subject to formal legal validation.

### 11. Founding offer / early access

Show a transparent founding-cohort concept without inventing final commercial terms. Include:

- Who it is for.
- What is included.
- Limited assisted onboarding capacity.
- No-card early application.
- Clear expectation that participants provide structured feedback.

Use a placeholder for final price if it is not approved.

### 12. FAQ

Answer:

- Preciso cancelar meu sistema atual imediatamente?
- É possível importar meus pacientes?
- O paciente precisa pagar?
- Funciona no celular?
- A IA cria planos sozinha?
- Como os dados dos pacientes são protegidos?
- Posso cancelar e exportar meus dados?
- Serve para nutrição clínica e esportiva?

### 13. Conversion form

Keep it short:

- Name.
- Professional email.
- WhatsApp.
- Current software.
- Approximate active-patient range.
- Main reason for switching.
- Consent to be contacted.

Do not collect clinical patient data on the landing page.

### 14. Footer

- Product links.
- Privacy.
- Terms.
- Security contact.
- Social channels placeholders.
- Clear company/legal placeholders.

## Required landing-page interactions

- Sticky navigation that remains restrained.
- Product-preview tabs.
- Early-access CTA opens or scrolls to the form.
- Form validation and success state.
- FAQ accordion.
- Responsive mobile layout.
- Accessible focus states and keyboard navigation.

## Landing-page acceptance criteria

- A current WebDiet/Dietbox/Nutrium customer understands the switching value in under ten seconds.
- The primary CTA is unambiguous.
- The page demonstrates the product with realistic screens rather than relying on abstract illustrations.
- The Growth capability is meaningful but does not eclipse clinical care.
- AI is differentiated through workflow and control, not chatbot marketing.
- No unverifiable testimonial, customer count, compliance seal, or outcome claim appears.

---

# Part 6 — Final prototype review prompt

After all four surfaces are generated, review the complete project as a principal product designer. Do not redesign it from scratch. Identify and fix inconsistencies.

Verify:

1. The same design tokens and components are used throughout.
2. Nutritionist screens are efficient and information-dense without becoming cluttered.
3. Patient screens are simpler, reassuring, and mobile-first.
4. The patient web and mobile applications share terminology and information architecture.
5. The landing page accurately previews the product that was designed.
6. AI-generated, professional-approved, and published states cannot be confused.
7. Clinical records, patient communications, marketing leads, and social-content drafts are appropriately separated.
8. Every primary flow has a start, success state, and relevant recovery/error state.
9. All visible content is idiomatic pt-BR.
10. Brazilian formats and realistic nutrition examples are used.
11. WCAG 2.2 AA contrast, focus, keyboard, screen-reader labeling, reduced-motion, and touch-target principles are represented.
12. No fake compliance, customer, or health-outcome claims were introduced.

Provide an organized screen inventory and a flow map for:

- Nutritionist switching and activation.
- First patient and first plan.
- Consultation and plan publication.
- Patient invitation and daily adherence loop.
- Approved substitution.
- Nutritionist monitoring and follow-up.
- Growth-content planning and lead conversion.
- Landing-page early-access conversion.

Finally, produce or update `DESIGN.md` so the resulting design language can be transferred consistently into the implementation stack.
