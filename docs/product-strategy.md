**PRODUCT STRATEGY REPORT**

**Nutrition Practice SaaS**

A Brazil-first, AI-assisted platform for nutritionists and patients

*Market benchmark • MVP definition • Architecture • Economics • 16-week execution plan*

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>Decision thesis</strong></p>
<p>Win switchers from legacy nutrition software with a safer migration path, dramatically faster care workflows, a patient adherence loop, and a focused practice-growth workspace. AI is embedded as an auditable copilot; it never autonomously prescribes.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

**Prepared for Igor Melo and the nutritionist co-founder**

Solo developer + embedded nutritionist co-founder • 12–16 weeks to production • Initial operating ceiling R\$1,000/month

Research cut-off: 18 August 2026 \| Language: English \| Product locale: pt-BR first

**How to read this report**

**Evidence labels used throughout:**

- Verified: directly observed on an official product, regulator, app-store, or provider source dated at the research cut-off.

- Observed: reported in a public review or community discussion; useful signal, not a representative sample.

- Inference: product opportunity or architectural conclusion derived from multiple sources and the founder constraints.

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>Legal boundary</strong></p>
<p>This is a product and engineering strategy, not a legal opinion. Brazilian counsel and a privacy professional should validate data-controller roles, the DPA, lawful bases, retention/deletion conflicts, international transfers, telenutrition consent, and professional-document requirements before production.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

**Contents**

| **1**  | Executive Summary        | **13** | Security & LGPD Strategy       |
|--------|--------------------------|--------|--------------------------------|
| **2**  | Market Research          | **14** | Infrastructure & Scalability   |
| **3**  | Competitor Benchmark     | **15** | Infrastructure / AI Cost Model |
| **4**  | User Pain Points         | **16** | Business Model & Pricing       |
| **5**  | Market Opportunities     | **17** | UX / Core Journeys             |
| **6**  | Differentiation Strategy | **18** | Engineering & Testing          |
| **7**  | AI Strategy              | **19** | Product & Technical Metrics    |
| **8**  | MVP Definition           | **20** | Architecture Decision Records  |
| **9**  | Post-MVP Roadmap         | **21** | Risk Register                  |
| **10** | Technology Stack         | **22** | Sprint Plan                    |
| **11** | System Architecture      | **23** | MVP Launch Checklist           |
| **12** | Data Architecture        | **24** | Open Questions / Decisions     |

# **1. Executive Summary**

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>Recommendation in one sentence</strong></p>
<p>Build a modular-monolith SaaS for independent Brazilian clinical nutritionists switching from WebDiet, Dietbox, or similar tools: migration-first onboarding, a fast consultation-to-plan workflow, a patient adherence PWA, and an ethics-aware practice-growth copilot.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

The market is large enough for a focused vertical SaaS. The CFN homepage reports 202,903 registered nutritionists in its 2026 second-quarter snapshot. Incumbents are feature-rich and trusted, but current app-store reviews still surface instability, buried logging flows, editing friction, and weak mobile execution. New AI-first entrants prove that automatic diet generation is already becoming a baseline claim rather than a durable moat.

**Source:** [CFN professional statistics](https://cfn.org.br/) — 202,903 nutritionists displayed for Q2 2026

The product wedge should therefore be neither “another diet calculator” nor “ChatGPT for nutritionists.” It should own the complete business and care loop:

- Switch safely: assisted import, validation, an explicit migration report, and exportability from day one.

- Work faster: pre-consult summary → assessment → plan draft → professional approval → publication.

- Improve outcomes: low-friction patient check-ins, substitutions, progress, and an attention queue for the nutritionist.

- Grow the practice: positioning, weekly/monthly content planning, compliant drafts, a public profile/lead form, and follow-up tasks.

- Acquire intentionally: a conversion-focused landing page moves qualified switchers from problem awareness → proof → early-access application → demo/pilot.

- Create trust: no autonomous prescription, clear AI labels, approval gates, provenance, audit logs, and privacy-by-design.

## **Founding-team advantage**

The founding team is not a solo domain bet: the developer’s wife is a practicing nutritionist, will act as co-founder, currently uses WebDiet, and can provide continuous workflow observation plus trusted access to early participants. Treat her as Design Partner Zero and product co-owner. Her evidence accelerates iteration, but it must not replace independent validation from external switchers.

## **Founder-constrained decision**

With one developer, one embedded nutritionist co-founder, a 12–16 week deadline, fewer than five external pilot contacts, and a R\$1,000 monthly operating cap, microservices would reduce delivery probability without improving the first customer outcome. The recommended backend is a Java 25 + Spring Boot 4 modular monolith, paired with Next.js/TypeScript and PostgreSQL. Domain boundaries and an outbox prepare later extraction, but there is no Kubernetes, Kafka, Redis, GraphQL, or vector database in the MVP.

## **Expected launch**

| **Milestone**        | **Target**                | **Definition**                                                                                                                                                           |
|----------------------|---------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| First deployed slice | End of week 1             | Public landing page + early-access capture, production-like deployment pipeline, authenticated product shell, and one mocked end-to-end workflow available for feedback. |
| Prototype validation | End of Sprint 2 / week 6  | Nutritionist co-founder plus at least five external interviews; tested migration, plan-builder, and patient-Today flows.                                                 |
| Private beta         | End of Sprint 5 / week 12 | Core professional and patient loop usable with real pilot data under signed beta terms.                                                                                  |
| Production MVP       | End of Sprint 7 / week 16 | Paid switcher onboarding, security/restore drills, observability, support runbook, and launch gates passed.                                                              |

# **2. Market Research**

## **Market shape**

Brazil has a mature professional-software category: WebDiet and Dietbox combine prescription, patient apps, scheduling, education, and communication; smaller products compete on price or AI; adjacent tools address WhatsApp conversion, social content, or no-code paid programs. International platforms show that the category can expand from meal planning into an operating system for a health practice.

| **Signal**                    | **Verified evidence**                                                                                                                                            | **Strategic meaning**                                                                                         |
|-------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| Large professional base       | CFN displays 202,903 nutritionists (Q2 2026).                                                                                                                    | A narrow initial segment can support a meaningful SaaS without serving every nutrition specialty.             |
| Incumbent scale               | Dietbox patient app: 1M+ Google Play downloads and 119K reviews; WebDiet patient app: 1M+ downloads.                                                             | Switchers expect a mature patient experience and cannot tolerate lost records or downtime.                    |
| AI substitution pressure      | CFN warned in Jan 2026 about illegal plans and nutrition guidance by unqualified people; current reporting describes consumers using general chatbots for diets. | Position the product as the professional-controlled alternative: personalization, accountability, and safety. |
| Practice growth is fragmented | Dietbox integrates Canva and practitioner discovery; WebDiet sells education/marketing; specialist tools automate content or WhatsApp funnels.                   | Growth is a real job-to-be-done, but a full marketing suite would be an MVP trap.                             |
| AI is already crowded         | WebDiet, Dietbox, NutriAssist, Cibus, PrescrIA Pro, and other emerging products market AI-assisted plans or summaries.                                           | Differentiation must be workflow quality, evidence, governance, adherence, and migration—not model access.    |

**Source:** [Dietbox Google Play](https://play.google.com/store/apps/details?hl=pt_BR&id=com.craftbox.dietbox)

**Source:** [WebDiet Google Play](https://play.google.com/store/apps/details?hl=pt_BR&id=br.com.webdiet.webdiet)

**Source:** [CFN warning on unqualified digital nutrition guidance](https://cfn.org.br/exercicio-ilegal-da-profissao/)

## **Research limitations**

Competitor pages use promotions, billing toggles, and geo-specific offers; prices below are snapshots, not promises. Public reviews over-represent strong experiences, and vendor claims were not penetration-tested or clinically evaluated. Emerging 2026 websites sometimes make unverified adoption, security, or accuracy claims; this report treats them as messaging evidence only.

# **3. Competitor Benchmark**

## **Brazil: major and emerging platforms**

| **Product**               | **Audience / model**                 | **Price snapshot**                                                                              | **Professional / patient experience**                                                           | **Growth & AI**                                               | **Evidence status**                             |
|---------------------------|--------------------------------------|-------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|---------------------------------------------------------------|-------------------------------------------------|
| WebDiet                   | Nutritionists; individual plans      | Premium: R\$49.90/mo first 3 months, then R\$94.90; Black R\$139.90                             | Broad prescription methods, TACO/TBCA/manufacturer foods, patient app, diary, goals, scheduling | Clara AI on Black; courses, Canvas, WhatsApp/Google Calendar  | Official, 18 Aug 2026                           |
| Dietbox                   | Nutritionists/students; subscription | Promo R\$49.90/mo first 3 months; subsequent price displayed around R\$88–92 depending selector | Professional + patient apps, USDA/food tables, plans, anthropometry, diary, chat, appointments  | Assistant, WhatsApp, Canva, discovery, Google Calendar        | Official; price UI variable                     |
| NutriAssist               | AI-first Brazilian nutritionists     | R\$99.90 monthly; R\$79.90/mo annual equivalent; credit packs                                   | Clinical chat, diet editor, TACO/TBCA/USDA, exams, forms, calendar, WhatsApp bot                | Strong contextual AI/RAG and workflow messaging               | Vendor-verified feature/pricing claims          |
| Salutes                   | Health practices; all-in-one         | Promotional page: R\$97/mo                                                                      | Agenda, chart, plan, assessments, finance, CRM, WhatsApp, patient app                           | AI writing/assistant/lead bots                                | Vendor claims; limited independent review       |
| Avanutri Online           | Nutritionists; protocol depth        | R\$130/mo official store result; six-month trial claimed                                        | Extensive protocols/indicators, food base, substitutions, patient diary/chat                    | Less differentiated AI/growth story                           | Official/vendor claims; food count inconsistent |
| CoreDiet / niche entrants | Emerging Brazilian products          | Not consistently published                                                                      | Clinical suite, patient app, specialist modules                                                 | CorePage/Marketing AI; several entrants claim AI plans/photos | Treat adoption/security claims as unverified    |

**Source:** [WebDiet pricing](https://webdiet.co/assine.php?tg=true)

**Source:** [Dietbox product and pricing](https://dietbox.me/pt-BR)

**Source:** [NutriAssist pricing and features](https://www.nutriassist.com.br/)

**Source:** [Salutes](https://salutes.com.br/)

**Source:** [Avanutri Online](https://beta.avanutrionline.com/)

## **International benchmarks**

| **Product**     | **Price snapshot**                                                                                              | **Strengths worth learning from**                                                            | **Gap / caution for Brazil**                                                                        |
|-----------------|-----------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|
| Healthie        | Core US\$19.99; Essentials US\$49.99; Plus US\$129.99; Group US\$149.99+                                        | Practice OS: scheduling, payments, charting, telehealth, client portal/mobile, organizations | US healthcare/billing orientation; broad scope and higher complexity                                |
| Practice Better | Free 3 clients; US\$35 Starter; US\$59 Professional; US\$89 Plus; US\$145 Team                                  | Strong programs, journals, goals, forms, mobile, team operations; metered AI charting        | Food planning partly depends on higher tiers/integration; pricing jump complaints appear in reviews |
| That Clean Life | US\$30 Starter; US\$60 monthly or US\$35 annual-equivalent Plus                                                 | Beautiful plan delivery, templates, filters, dietary constraints, nutrition insights         | Narrower practice management; no free trial                                                         |
| NutriAdmin      | Approx. US\$34.99 Basic; US\$49.99 Popular; US\$74.99 Pro monthly                                               | CRM, portal, questionnaires, reports, meal planning, Google Calendar                         | AI recipe add-on/feature segmentation; international food expectations                              |
| Nutrium         | Current official public pricing unclear; a Mar 2026 third-party screenshot reported US\$15–25 annual-equivalent | Integrated professional workflow and patient follow-up across many countries/languages       | Pricing/status uncertainty; do not base economics on third-party snapshot                           |

**Source:** [Healthie pricing](https://www.gethealthie.com/healthie-pricing)

**Source:** [Practice Better pricing](https://practicebetter.io/pricing)

**Source:** [That Clean Life pricing](https://thatcleanlife.com/pricing)

**Source:** [NutriAdmin pricing](https://nutriadmin.com/pricing)

**Source:** [Nutrium review with observed March 2026 pricing](https://www.promealplan.com/en/blog/nutrium-review-2026) — Third-party competitor; use cautiously

## **Patient/mobile evidence**

| **Observed source**                  | **Finding**                                                                                                                          | **Product implication**                                                                                 |
|--------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| Dietbox Google Play, Jun 2026        | A reviewer described meal logging as buried under four UI levels, leading to delayed batch completion.                               | Make Today the default patient screen; one-tap check-in and substitution, not a generic dashboard.      |
| Dietbox App Store, 2025–26           | Reviews mention interface/UX problems, instability, editing problems, and login issues; updates frequently address diary/plan/login. | Reliability and task completion are marketable features. Track crash-free sessions and completion time. |
| WebDiet Google Play, Aug 2026        | A reviewer requested direct recipe access from the meal instead of leaving the plan and searching a large list.                      | Every plan object should be contextual and deep-linked—recipe, substitution, rationale, and logging.    |
| WebDiet Google Play, Aug 2026        | A reviewer requested supplement/GI/Bristol-style longitudinal logging and symptom correlations.                                      | Post-MVP opportunity: configurable clinical check-ins rather than universal calorie logging.            |
| Dietbox professional App Store, 2025 | Visible reviews report disappearing plan options, editing failures, and multi-day impact on care.                                    | Autosave, explicit version history, idempotency, and restore testing are product differentiation.       |

**Source:** [Dietbox professional App Store reviews](https://apps.apple.com/br/app/dietbox-para-profissionais/id1089653522?platform=ipad&see-all=reviews)

## **Market expectations, advantages, and gaps**

| **Category**                   | **Capabilities**                                                                                                                                                                                                                               |
|--------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Must-have expectations         | Patient records; assessment/anamnesis; measurements; calculated/free plans; food database; substitutions; recipes or meal guidance; patient plan access; progress; reminders; basic communication; export/PDF.                                 |
| Stronger-competitor advantages | WhatsApp/Calendar/Canva; mobile apps; templates; courses; team roles; telehealth; finance; automated forms; structured programs; protocol libraries; AI summaries/plans.                                                                       |
| Repeated gaps                  | Complex or fragile UX; plan creation still time-consuming; logging friction; context lost between plan/recipe/substitution; adherence signals not actionable; communication fragmented; mobile reliability; opaque migration/export; AI trust. |
| White-space opportunity        | Switcher-grade migration + time-to-plan leadership + adherence attention queue + ethics-aware practice growth + auditable AI approvals.                                                                                                        |

# **4. User Pain Points**

| **Persona / job**           | **Pain**                                                           | **Underlying cause**                                       | **Design response**                                                                                         |
|-----------------------------|--------------------------------------------------------------------|------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| Switcher nutritionist       | Fear of losing records, templates, and continuity                  | Weak exports, proprietary formats, manual re-entry         | Import workspace, mapping preview, reconciliation report, rollback, parallel-read period, permanent export. |
| Nutritionist / consultation | Too much work before and after the appointment                     | History scattered; repeated typing; slow plan construction | Pre-consult brief, structured note draft, reusable protocols, plan versioning, keyboard-first editor.       |
| Nutritionist / growth       | Inconsistent demand and difficulty turning expertise into clients  | No weekly system; content and leads live in separate tools | Practice-growth cockpit: positioning, content calendar, drafts, lead form, follow-up tasks.                 |
| Nutritionist / monitoring   | Cannot tell who needs attention                                    | High-volume diaries and messages without prioritization    | Attention queue based on missed check-ins, questions, deviations, and upcoming follow-ups.                  |
| Patient / daily use         | Logging feels like work; plan is hard to navigate                  | Generic dashboards, deep menus, calorie-centric design     | Today-first PWA, one-tap adherence, contextual substitutions/recipes, optional detail.                      |
| Patient / questions         | General AI is instant but unsafe and ignores the professional plan | No plan-grounded, 24/7 explanation layer                   | Read-only plan assistant with approved sources, clear escalation, and no autonomous modifications.          |

# **5. Market Opportunities**

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>1 — Migration as a product</strong></p>
<p>Targeting switchers changes onboarding from a form into a trust event. Offer assisted import, evidence of what migrated, and a no-lock-in export pledge.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>2 — Practice growth tied to clinical retention</strong></p>
<p>Do not sell vanity content volume. Connect positioning → content → lead → consultation → ongoing care → renewal, while respecting CFN advertising rules.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>3 — Adherence intelligence</strong></p>
<p>Convert lightweight patient check-ins into a small, explainable attention queue. The nutritionist acts on exceptions instead of reading every event.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>4 — Professional-controlled AI</strong></p>
<p>General chatbots compete on immediacy. The platform competes on context, professional accountability, auditability, safety gates, and follow-through.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>5 — Brazilian food and routine reality</strong></p>
<p>TACO/TBCA provenance, household measures, rice/beans, restaurant-by-weight, budget and preparation constraints, and weekend patterns matter more than an enormous undifferentiated food catalog.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

# **6. Product Differentiation Strategy**

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>Positioning</strong></p>
<p>The modern practice OS for Brazilian nutritionists who want to switch software, save clinical time, keep patients engaged, and grow ethically—without surrendering judgment to AI.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

| **Pillar**                        | **Promise**                                                   | **Proof metric**                                                      | **MVP expression**                                                |
|-----------------------------------|---------------------------------------------------------------|-----------------------------------------------------------------------|-------------------------------------------------------------------|
| Switch without fear               | Your history and templates come with you—and can leave again. | % imports reconciled; migration defects; time to first usable patient | CSV/PDF import, mapping, exceptions, export bundle                |
| From consultation to plan quickly | Finish the work while the context is fresh.                   | Median time from consultation end to published plan                   | Structured assessment, fast plan builder, draft summary, approval |
| Know who needs you                | One attention queue, not an ocean of logs.                    | % flagged patients acted on; adherence response time                  | Check-ins, missed-event rules, explainable flags                  |
| Grow with integrity               | Turn expertise into a repeatable pipeline.                    | Content plan completion; lead-to-consult conversion                   | Positioning brief, calendar, compliant drafts, lead form/tasks    |
| AI under professional control     | Every clinical output is traceable and approved.              | AI acceptance/edit/rejection; safety incidents                        | Structured outputs, labels, provenance, audit, publish gates      |

## **Practice-growth scope boundary**

MVP includes strategy and workflow, not a social network scheduler. It creates a monthly theme, weekly plan, evidence-backed draft, caption variants, short-video outline, and call-to-action; checks the draft against a ruleset; stores approval status; and connects a public profile/lead form to follow-up tasks. It does not auto-post, scrape social networks, buy ads, promise outcomes, display public fees/promotions, or generate before/after content.

**Source:** [CFN Code of Ethics, Resolution 599/2018](https://cfn.org.br/wp-content/uploads/resolucoes/Res_599_2018.html)

**Source:** [CRN-8 social-media and marketing FAQ](https://crn8.org.br/perguntas-frequentes/) — Public prices/promotions/sweepstakes and before/after guidance

# **7. AI Strategy**

## **Principles**

- Deterministic first: calculate nutrients, constraints, allergens, and totals with code and curated data—not an LLM.

- Grounded second: patient answers use the active professional-approved plan and approved educational content only.

- Structured outputs: JSON Schema for every clinical draft; refuse or escalate when the schema/safety policy fails.

- Human in the loop: the nutritionist approves any item that changes care, becomes part of the record, or is sent as clinical guidance.

- Minimum necessary data: pseudonymize where practical; do not include identity fields when not needed; redact logs.

- Auditable by design: model snapshot, prompt/policy version, input references, output, cost, latency, safety flags, reviewer, and final edits.

- Provider-portable, not provider-agnostic fantasy: one production provider behind an internal interface; add a second only when risk or economics justify it.

## **Capability ranking**

*Score = 30% user value + 25% differentiation + 15% implementation ease + 10% low operating cost + 20% low safety risk. Scores are strategic estimates (1–5), not empirical results.*

| **Rank** | **Capability**                        | **Value** | **Diff.** | **Ease** | **Cost** | **Safety** | **Score** | **Decision**                |
|----------|---------------------------------------|-----------|-----------|----------|----------|------------|-----------|-----------------------------|
| 1        | Pre-consult/history summary           | 5         | 4         | 5        | 5        | 4          | 4.55      | MVP                         |
| 2        | Notes → structured consultation draft | 5         | 4         | 4        | 5        | 4          | 4.40      | MVP, typed notes            |
| 3        | Adherence digest + explainable flags  | 5         | 5         | 3        | 4        | 4          | 4.35      | MVP rules + summary         |
| 4        | Ethics-aware content planner/drafts   | 4         | 5         | 4        | 4        | 4          | 4.25      | MVP-lite                    |
| 5        | Plan-grounded patient Q&A             | 5         | 5         | 3        | 4        | 2          | 3.95      | Controlled beta             |
| 6        | Smart substitutions                   | 5         | 4         | 3        | 4        | 3          | 3.90      | Rules first; AI explanation |
| 7        | Meal-plan draft from constraints      | 5         | 3         | 3        | 3        | 2          | 3.35      | MVP professional-only       |
| 8        | Audio consultation scribe             | 4         | 3         | 2        | 3        | 3          | 3.05      | Post-MVP                    |
| 9        | Meal photo nutrient estimation        | 4         | 3         | 2        | 2        | 1          | 2.45      | Avoid initially             |
| 10       | Autonomous patient coach/prescriber   | 4         | 4         | 1        | 2        | 1          | 2.40      | Do not build                |

## **Approval policy**

| **AI output**                          | **Professional approval**                           | **Patient exposure**  | **Guardrail**                                            |
|----------------------------------------|-----------------------------------------------------|-----------------------|----------------------------------------------------------|
| History/pre-consult summary            | Review before consultation; corrections logged      | No                    | Citations to source fields; missing-data flags           |
| Structured consultation note           | Required before record finalization                 | No                    | Draft watermark; immutable final version                 |
| Nutrition-plan draft                   | Required before publish; never bulk auto-publish    | Only approved version | Constraint engine, allergen/restriction validation       |
| Substitution inside pre-approved group | Rule-level pre-approval; nutritionist defines group | Yes                   | Nutrient tolerance, restrictions, serving conversion     |
| Substitution outside group             | Case-by-case required                               | Not until approved    | Escalate; no invented foods/portions                     |
| Patient plan explanation               | No per-message approval if within approved plan     | Yes, labeled AI       | Retrieval allowlist; no diagnosis/change; escalation     |
| Lab/supplement interpretation          | Always required                                     | Not MVP               | Professional-only, source and uncertainty displayed      |
| Social content                         | Professional approval before copy/export            | Public after approval | CFN ruleset, evidence links, no claims/fees/before-after |

## **Model and operational design**

Start with the OpenAI Responses API behind an internal AiGateway interface. Use a cost-sensitive model for patient explanations/classification and a stronger balanced model for professional summaries/drafts; route by task, not by a user-facing model picker. As of 18 Aug 2026, OpenAI lists GPT-5.6 Luna at US\$0.20/US\$1.20 and Terra at US\$2/US\$12 per 1M input/output tokens. Use model snapshots and a task-level budget.

OpenAI states that API data is not used for training unless the customer opts in, but default abuse-monitoring logs may be retained for up to 30 days. Set store:false, complete a DPA and international-transfer review, minimize health data, and assess Modified Abuse Monitoring or Zero Data Retention eligibility. Do not market “zero retention” unless the account and endpoint configuration actually provides it.

**Source:** [OpenAI model catalog and pricing](https://developers.openai.com/api/docs/models)

**Source:** [OpenAI data controls](https://developers.openai.com/api/docs/guides/your-data)

**Source:** [OpenAI Structured Outputs](https://developers.openai.com/api/docs/guides/structured-outputs)

## **Evaluation and prompt-injection defenses**

- Golden sets: 100+ de-identified cases covering allergies, diabetes, pregnancy, renal risk, eating-disorder signals, pediatric cases, cultural constraints, and ambiguous notes.

- Measure: schema validity, groundedness, constraint violations, omission rate, unsafe recommendation rate, nutritionist edit distance, acceptance/rejection, latency, and cost.

- Prompt injection: treat uploaded documents and patient messages as untrusted data; separate instructions from content; tool allowlists; no arbitrary URL/browser tools; escape/quote retrieved text; output policy after generation.

- Red-team before every material prompt/model change; version and canary; disable a feature independently through flags.

- Patient assistant must have an emergency/clinical escalation path and clearly state when the nutritionist—not the AI—must respond.

# **8. MVP Definition**

## **MVP outcome**

A qualified switcher can discover the product through a clear landing page, request early access, import a small real practice, complete an assessment, publish a safe plan, let a patient follow/check in from a mobile web app, see who needs attention, and run a simple weekly growth routine. Everything else is subordinate.

| **MVP module**           | **Included**                                                                                                                                                                     | **Explicitly not included**                                                                               |
|--------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| Acquisition landing page | pt-BR positioning; switcher-specific pain/proof; product workflow preview; founding offer; FAQ; early-access/demo form; privacy/consent; source attribution and funnel analytics | Generic corporate brochure; SEO content engine; ad manager; self-service checkout before offer validation |
| Switcher onboarding      | Organization + professional profile; manual CRN review; CSV patient import; PDF/document batch; mapping preview; exception report; export                                        | Automatic scraping of competitor accounts; every proprietary format; white-glove migration at scale       |
| Patient/clinical record  | Patient profile, consent, intake/anamnesis, core history, consultation note, goals, measurements, progress photos                                                                | Full EHR, every protocol, lab interpretation, prescriptions for all specialties                           |
| Nutrition plan           | TACO-seeded food search with provenance; meals/items/household measures; totals; templates; versioning; substitutions; PDF                                                       | TBCA bulk ingest without license; barcode catalog; recipe marketplace; exotic optimization engine         |
| Patient PWA              | Invitation, Today, plan, contextual substitution, one-tap adherence, measurements/photos, questions, reminders                                                                   | Native app, wearables, offline conflict engine, calorie-photo estimation                                  |
| Adherence                | Configurable check-in, missed-check-in rules, attention queue, simple progress graphs                                                                                            | Predictive medicine, gamification economy, generalized behavioral scoring                                 |
| AI                       | History summary, typed-notes structuring, professional plan draft, adherence digest, controlled plan Q&A beta                                                                    | Autonomous diagnosis/prescription, lab/supplement advice, audio scribe, open-web patient bot              |
| Practice growth          | Positioning profile, monthly/weekly content planner, AI drafts + ethics checklist, public profile/lead form, follow-up tasks                                                     | Auto-posting, ad buying, social inbox, full CRM, WhatsApp sales bot                                       |
| Operations               | Basic appointment record + email reminders; support/admin; audit; analytics; manual subscription administration                                                                  | Telehealth video, integrated payment split, insurance, finance/accounting                                 |

## **MVP acceptance guardrails**

- A qualified landing-page visitor understands the switcher promise and can request early access in ≤2 minutes; source, consent, and conversion events are recorded.

- A new switching nutritionist reaches a usable imported patient in ≤30 minutes for the supported template.

- A trained user creates and publishes a standard plan in ≤15 minutes excluding clinical reasoning and consultation time.

- A patient finds today’s meal and records adherence in ≤10 seconds from opening the PWA.

- No clinical AI output reaches a patient unless the approval policy permits it; plan changes always require professional approval.

- Tenant-isolation, consent, export, deletion workflow, audit, backup restore, and incident runbook tests pass before real data.

# **9. Post-MVP Roadmap**

| **Horizon**          | **Capabilities**                                                                                                                                                                                    | **Why then**                                                                             |
|----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| Post-MVP / retention | Native Expo patient app; secure asynchronous messaging; flexible check-ins; recipes/shopping; Google Calendar; better migrations; renewal/reactivation; content performance/manual attribution      | Adds retention after the core care loop and migration wedge are proven.                  |
| Growth / PMF         | Clinic roles; reception; multi-professional teams; WhatsApp Business integration; billing/PIX; audio scribe; lab extraction; protocol knowledge base; TBCA commercial agreement; branded programs   | Requires volume, support capacity, legal review, and proven willingness to pay.          |
| Advanced             | Wearables; population cohorts; experiments; richer AI routing; enterprise SSO; APIs/webhooks; read replicas; regional expansion; specialty modules                                                  | Only after repeatable acquisition and strong retention.                                  |
| Avoid initially      | Microservices, Kubernetes, Kafka, a proprietary general food database, autonomous AI care, meal-photo calorie promises, marketplace, insurance billing, video infrastructure, full social scheduler | High complexity, regulation, cost, or weak validation relative to the 16-week objective. |

# **10. Recommended Technology Stack**

| **Layer**     | **Recommendation**                                                                         | **Why this product**                                                                                         | **Rejected for MVP**                                                          |
|---------------|--------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------|
| Web           | Next.js App Router + TypeScript; responsive PWA                                            | Fast React delivery, routing/rendering flexibility, accessibility, future shared TS packages                 | Separate web products; heavy micro-frontends                                  |
| Backend       | Java 25 LTS + Spring Boot 4.1 + Spring Modulith                                            | Matches founder expertise; strong security/validation/transactions/observability; explicit module boundaries | NestJS is viable but offers insufficient advantage to discard Spring depth    |
| Mobile        | Expo/React Native after MVP                                                                | Best fit with React/TS; first-class monorepo support; share tokens, API client, schemas, i18n—not whole UI   | Flutter/native teams before PMF                                               |
| Database      | PostgreSQL; Flyway migrations                                                              | Transactional clinical model, JSON where useful, constraints, mature portability                             | MongoDB as primary; database-per-tenant                                       |
| API           | REST + OpenAPI 3; generated TS client; webhooks later                                      | Clear resource workflows, caching/idempotency, easy mobile/integration support                               | GraphQL adds authorization/caching/operational surface without a current need |
| Auth          | Supabase Auth; PKCE; own authorization tables; TOTP MFA                                    | Managed identity, São Paulo region option, mobile/web support, low MVP cost                                  | Self-hosted Keycloak/Auth server; business rules in provider claims           |
| Files         | Supabase Storage initially, private buckets + signed URLs; offsite object inventory/backup | Co-located managed data plane; simple client uploads                                                         | Database BLOBs; public buckets                                                |
| Jobs          | Transactional outbox + PostgreSQL-backed jobs/JobRunr                                      | Reliable email, AI, export, import without a new broker                                                      | Kafka/SQS/Redis queue before load requires it                                 |
| Cache/search  | None initially; PostgreSQL indexes and full-text/trigram                                   | Avoid invalidation and extra infrastructure                                                                  | Redis/vector/search service without measured need                             |
| AI            | OpenAI Responses through AiGateway; Structured Outputs; task router                        | Current strong multilingual model range and governance primitives                                            | Hard-coded vendor calls across domains                                        |
| Observability | OpenTelemetry/Micrometer + Sentry; sanitized product events                                | One trace ID from UI/API/job/AI; low-cost free tiers                                                         | PHI in logs/session replay                                                    |

**Source:** [Spring Boot official project](https://spring.io/projects/spring-boot/)

**Source:** [Oracle Java SE support roadmap](https://www.oracle.com/java/technologies/java-se-support-roadmap.html) — Java 25 is LTS

**Source:** [Next.js App Router docs](https://nextjs.org/docs/app)

**Source:** [Expo monorepo support](https://docs.expo.dev/guides/monorepos/)

# **11. System Architecture**

## **Logical architecture**

| **Client edge**                                                                      | **Application core**                                                                         | **Managed data/services**                                           |
|--------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|---------------------------------------------------------------------|
| Nutritionist web • Patient responsive PWA • Future Expo mobile • Future integrations | REST API • AuthZ policy • domain modules • outbox/jobs • AI gateway • audit/analytics events | PostgreSQL/Auth/Storage • email • OpenAI • Sentry/metrics • CDN/DNS |

*Request path: client obtains an identity token → backend validates identity and resolves organization/membership → domain service enforces relationship/role and performs one transaction → audit/outbox rows commit atomically → worker performs notification/AI/export → result is stored and emitted as a sanitized product event.*

## **Modular monolith boundaries**

| **Module**         | **Owns**                                                                      | **May depend on**                          |
|--------------------|-------------------------------------------------------------------------------|--------------------------------------------|
| Identity & Tenancy | User, organization, membership, role, invitation, care-team access            | Auth provider adapter                      |
| Growth             | Professional profile, positioning, content plan/draft, lead, follow-up task   | Identity; Scheduling; AI                   |
| Patient            | Person, organization-patient relationship, intake, consent, goals             | Identity; Audit                            |
| Clinical           | Consultation, note versions, assessment, measurement, progress media          | Patient; Documents                         |
| Nutrition          | Food provenance, portions, recipes later, plan/version/meal/item/substitution | Patient; Clinical                          |
| Engagement         | Check-ins, adherence events, attention flags, patient questions               | Patient; Nutrition; Notification           |
| Scheduling         | Appointment and reminder state                                                | Patient; Notification                      |
| AI Governance      | Interaction, policy/prompt/model version, evaluation, review decision, cost   | Read-only projections from allowed modules |
| Documents          | Object metadata, signed access, export jobs                                   | Identity/Tenancy                           |
| Audit & Compliance | Append-only audit, data request/case, retention holds                         | Receives events; no domain writes          |

## **Multi-tenancy**

MVP tenancy is shared database/shared schema. Every professional receives an Organization even when solo; clinics later add memberships. Organization-owned tables carry organization_id, and unique/index definitions include it. Application checks are mandatory; PostgreSQL row-level security is defense in depth for exposed Supabase paths. Never trust a tenant ID supplied by the client without resolving it from the authenticated membership.

| **Option**                 | **Strength**                                                   | **Weakness**                                   | **Decision**                |
|----------------------------|----------------------------------------------------------------|------------------------------------------------|-----------------------------|
| Shared DB/schema           | Lowest cost; simplest migrations/reporting; best solo velocity | Isolation depends on rigorous policy/tests     | MVP recommendation          |
| Shared DB/separate schemas | Some logical separation                                        | Migration explosion; awkward pooling/analytics | Do not use                  |
| Separate database          | Strong isolation/custom residency                              | High cost and operational burden               | Enterprise option after PMF |

## **Authentication and authorization**

- Professional: email/password or passwordless with verified email; require TOTP MFA before access to real patient data; step-up for exports and sensitive admin actions.

- Patient: invitation-bound signup with PKCE; short access token, rotating refresh token, device/session view, revocation; no patient enumeration.

- RBAC + relationship policy: OWNER, NUTRITIONIST, ASSISTANT (future), PATIENT, SUPPORT_BREAK_GLASS. Roles do not replace patient-care relationship checks.

- Break-glass support access disabled by default, time-limited, reason-required, fully audited, and visible to organization owner.

- MFA recovery is a product flow: verified recovery, cooldown, audit, and alerts—not a database edit by support.

# **12. Data Architecture**

## **Core relationships**

| **Aggregate / entity**                   | **Key relationships and invariants**                                                                                                                     |
|------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| Organization                             | Owns practice data. Solo professional is still a one-member organization. Future clinic staff join through Membership.                                   |
| User / ProfessionalProfile               | User is identity; profile stores CRN region/number/verification. No clinical data in auth claims.                                                        |
| PatientPerson / CareRelationship         | A person can have separate relationships with multiple organizations. Records do not merge across organizations without a defined consent/legal process. |
| ConsentRecord                            | Purpose, text/version, lawful-basis context, channel, timestamp, withdrawal, guardian where applicable. Consent is not used as a universal legal basis.  |
| Consultation / ClinicalNoteVersion       | Consultation anchors assessment. Final notes are immutable; amendments create linked versions with author/reason.                                        |
| Measurement / Goal / ProgressMedia       | Time-series observations with method, unit, source, author; media in private storage with metadata in DB.                                                |
| NutritionPlan / PlanVersion              | Draft → review → published → superseded. Patient sees only published versions. Plan item references FoodVersion/portion, not mutable display text alone. |
| Food / FoodVersion / FoodSource          | Canonical item plus source/version/provenance, nutrient values and qualifiers, household portions, aliases, branded/custom scope.                        |
| AdherenceEvent / CheckIn / AttentionFlag | Raw events are separate from derived flags. Every flag records rule/model, evidence, severity, status, and resolution.                                   |
| ContentPlan / ContentDraft / Lead        | Growth data is separated from clinical data. A lead becomes a patient only through an explicit conversion and consent flow.                              |
| AiInteraction / AuditEvent               | AI stores task/policy/model/input references/output/review/cost; audit stores security-sensitive state changes. Neither is a general log dump.           |

## **Food data strategy**

TACO 4th edition is a useful open seed: the official publication permits total or partial reproduction when the source is cited, but it is dated (2011) and contains roughly 597 foods. TBCA is much richer and current (version 7.3 at research time; its site describes more than 5,700 foods in the preceding release and Brazilian recipes/household measures), but public search access is not the same as a commercial bulk-data/API license. Do not scrape TBCA into production without written permission or a legal/licensing conclusion.

**Source:** [TACO 4th edition PDF](https://cfn.org.br/wp-content/uploads/2017/03/taco_4_edicao_ampliada_e_revisada.pdf)

**Source:** [TBCA official site](https://www.tbca.net.br/)

**Source:** [FAO TBCA catalog entry](https://www.fao.org/food-composition/tables-and-databases/detail/%28brazil--2025%29-tabela-brasileira-de-composi%C3%A7%C3%A3o-de-alimentos-%28tbca%29/en)

| **Stage** | **Data approach**                                                                                                               | **Controls**                                                                           |
|-----------|---------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| MVP       | TACO ingestion after legal confirmation; custom foods; nutritionist-scoped foods; household portions; source/version visible    | Provenance required; no silent value override; import tests; citation in exports       |
| Post-MVP  | Written TBCA license/permission; USDA for international foods; branded source partnership or carefully licensed Open Food Facts | Per-source license registry; locale/brand confidence; duplicate resolution; moderation |
| Scale     | Canonical matching, source precedence, version migrations, validation sampling, issue reporting                                 | Stewardship workflow; change audit; backward reproducibility for old plans             |

# **13. Security & LGPD Strategy**

## **Legal/regulatory requirements vs recommendations**

| **Type**      | **Requirement / finding**                                                                                                                                            | **Product or engineering action**                                                                                                       | **Validation**              |
|---------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|-----------------------------|
| Law           | Health data is sensitive personal data under LGPD Art. 5; Art. 11 has specific lawful bases.                                                                         | Data inventory, purpose/basis register, minimum necessary collection; do not use legitimate interest as a generic sensitive-data basis. | Brazil privacy counsel      |
| Law           | Data-subject rights include confirmation, access, correction, portability, information, revocation, and deletion subject to legal retention.                         | Privacy request case workflow; export; correction; identity verification; legal hold/retention reason; response tracking.               | Counsel + privacy lead      |
| Law           | Security-by-design and incident duties apply; ANPD Resolution 15/2024 requires notice within three business days when relevant risk/damage exists.                   | Incident classification/playbook, evidence preservation, controller notification SLA, contact registry, tabletop exercise.              | Counsel; controller DPA     |
| Regulation    | Telenutrition requires active CRN, e-Nutricionista registration, information on limitations/fragilities, and consent before first remote service under CFN 760/2023. | Professional attestation, consent/version evidence, modality field, document/signature workflow if teleconsultation enters scope.       | CFN specialist/counsel      |
| Regulation    | CFN record rules and Law 13.787 establish long retention; CFN 594 specifies at least 20 years for physical records and rules for electronic records.                 | Retention schedule and legal holds; never equate account cancellation with clinical-record deletion; immutable final-note versions.     | Counsel; CRN interpretation |
| Regulation    | CFN advertising rules prohibit misleading/sensational claims, result guarantees, public fees/promotions/sweepstakes, and before/after use.                           | Content checklist and warnings; templates avoid claims; approval; evidence link; no automated publishing in MVP.                        | CFN/CRN marketing review    |
| Law           | International transfers must use LGPD mechanisms under ANPD Resolution 19/2024.                                                                                      | Vendor register, data-flow map, SCC/contract mechanism, subprocessor review, disclosure and transfer-impact review.                     | Brazil privacy counsel      |
| Best practice | Strong access, encryption, backup and monitoring are expected even where a precise configuration is not legislated.                                                  | TLS; managed encryption at rest; KMS/provider keys; MFA; least privilege; secret manager; SAST/DAST; restore drills.                    | Security review             |

**Source:** [Brazilian LGPD, Law 13.709/2018](https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709.htm)

**Source:** [ANPD security-incident communication](https://www.gov.br/anpd/pt-br/assuntos/comunicacao-de-incidentes-de-seguranca-cis)

**Source:** [ANPD international-transfer guidance](https://www.gov.br/anpd/pt-br/assuntos/assuntos-internacionais/transferencia-internacional-de-dados)

**Source:** [CFN telenutrition Resolution 760/2023 announcement](https://cfn.org.br/cfn-publica-resolucao-que-regulamenta-a-telenutricao/)

**Source:** [CFN Resolution 594/2017](https://cfn.org.br/wp-content/uploads/resolucoes/resolucoes_old/Res_594_2017.htm)

**Source:** [Law 13.787/2018 on health-record digitization/retention](https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/L13787.htm)

## **Controller/operator posture**

Likely structure: the nutritionist/clinic is controller for patient care data and the SaaS is operator; the SaaS is controller for its own accounts, billing, fraud/security, and possibly carefully defined product analytics. Roles can vary by purpose, so contracts and notices must be purpose-specific. Execute a DPA with instructions, subprocessors, security annex, incident obligations, international transfers, return/deletion, audit cooperation, and controller response support.

## **Security baseline before beta**

- Threat model tenant isolation, IDOR/BOLA, invitation takeover, object URL leakage, mass export, prompt injection, support access, and malicious file uploads.

- Encrypt in transit; provider encryption at rest; signed short-lived file URLs; virus/type/size scanning; private buckets; no public patient media.

- Least-privilege database/service accounts; production separated from development; no real data in non-production; secrets in managed store; dependency and container scanning.

- Append-only audit for login/MFA changes, role/membership, consent, record read/export, plan publish, note finalization/amendment, AI review, and support access.

- Logs and analytics contain stable pseudonymous identifiers—not names, notes, prompts, plan text, photos, or tokens. Disable session replay on clinical surfaces unless comprehensively masked and legally reviewed.

- Daily managed DB backup plus weekly tested logical export; object inventory and replication/export; documented RPO/RTO; quarterly restore drill initially.

# **14. Infrastructure & Scalability Strategy**

| **Area**    | **Stage 1 — Early MVP**                                                                               | **Stage 2 — PMF**                                                                   | **Stage 3 — Scale**                                                                        |
|-------------|-------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| Scale       | 10s–100s professionals; 100s–1,000s patients                                                          | 1,000s–10,000s users                                                                | 100,000s–millions                                                                          |
| Compute     | One Spring app/worker deployment on Fly.io GRU; min 1 instance, health restart; Next.js on Vercel Pro | 2+ API instances; separate worker process; autoscaling/load balancer                | Multi-AZ app pools; extract only measured hotspots; regional strategy                      |
| Database    | Supabase Pro PostgreSQL in São Paulo; pooled connections; daily backups                               | Larger compute; PITR if economics justify; read replica only for measured read load | HA/PITR mandatory; replicas; partition heavy event/audit tables; enterprise tenant options |
| Storage/CDN | Private Supabase Storage; signed URLs; Vercel/CDN for public assets                                   | Lifecycle policies; object replication/export; malware pipeline                     | Multi-region object strategy; CDN; media processing workers                                |
| Cache       | None                                                                                                  | Small Redis only for proven hot/cache/rate-limit use                                | Distributed cache if measured; strict invalidation ownership                               |
| Events/jobs | Postgres outbox + JobRunr                                                                             | Managed queue (SQS/Pub/Sub) when throughput/reliability warrants                    | Partitioned queues/events; Kafka only for demonstrated streaming needs                     |
| Monitoring  | Sentry, OTel/Micrometer, uptime, structured redacted logs, cost alerts                                | Central metrics/traces, SLOs, on-call rotation, synthetic journeys                  | Multi-region SLOs, capacity forecasting, security monitoring                               |
| DR          | RPO 24h / RTO 8h target; quarterly restore test                                                       | RPO ≤1h / RTO ≤2h; PITR; semiannual failover                                        | RPO minutes / RTO \<1h where business requires; regional drills                            |
| CI/CD/IaC   | GitHub Actions; test/build/scan/migrate/deploy; protected prod; small Terraform footprint             | Progressive deploy/canary; ephemeral previews with fake data                        | Policy-as-code, multi-account/environment, automated rollback                              |

**Source:** [Supabase regions](https://supabase.com/docs/guides/platform/regions) — São Paulo sa-east-1 available

**Source:** [Supabase pricing](https://supabase.com/pricing) — Pro US\$25/month; daily backups 7 days

**Source:** [Fly.io regions](https://fly.io/docs/reference/regions/) — São Paulo GRU supports Machines and Managed Postgres

**Source:** [Fly.io resource pricing](https://fly.io/docs/about/pricing/)

**Source:** [Vercel pricing](https://vercel.com/pricing) — Pro US\$20/month

## **Provider decision**

Use a deliberately small multi-provider setup: Vercel for web delivery/previews; Fly.io GRU for the Java workload; Supabase São Paulo for PostgreSQL/Auth/Storage. This is simpler and cheaper for a solo developer than a full AWS production stack in São Paulo. Isolate each provider behind standards: OpenAPI, OIDC/JWT adapter, PostgreSQL/Flyway, object-storage adapter, containers, and Terraform. Revisit consolidation when compliance procurement, private networking, SLAs, or R\$10k+ MRR justify it.

# **15. Estimated Infrastructure / AI Cost Model**

Planning exchange rate: US\$1 = R\$5.20 on 18 Aug 2026; budgets below add approximately 15% FX/tax/variance headroom. Provider taxes and regional compute markups can differ. Re-price before purchase.

**Source:** [USD/BRL reference](https://wise.com/us/currency-converter/usd-to-brl-rate/history) — About R\$5.20 on 18 Aug 2026

| **Cost item**                | **Launch assumption**                                     | **USD/month** | **BRL planning range**                   |
|------------------------------|-----------------------------------------------------------|---------------|------------------------------------------|
| Supabase Pro                 | 1 production project, micro compute credit, daily backups | 25            | R\$130–155                               |
| Vercel Pro                   | 1 developer; commercial production                        | 20            | R\$104–125                               |
| Fly.io app                   | 1–2 GB shared CPU in GRU; region/egress variance          | 15–25         | R\$78–155                                |
| Email                        | Resend free ≤3,000/mo, then Pro if required               | 0–20          | R\$0–125                                 |
| Sentry/analytics/uptime      | Free tiers; no clinical payloads                          | 0–10          | R\$0–62                                  |
| Backups/storage/domain/misc. | Small object volume, domain, offsite export               | 5–15          | R\$26–94                                 |
| AI                           | Hard task budgets; pilot to early paid usage              | 20–70         | R\$104–437                               |
| Total                        | Expected early range                                      | 85–185        | Approx. R\$500–950 including contingency |

**Source:** [Resend pricing](https://resend.com/pricing) — Free 3,000 emails/month; Pro US\$20

**Source:** [Sentry pricing](https://sentry.io/pricing/)

## **AI unit economics**

| **Task**                  | **Assumed tokens**     | **Model class** | **Approx. cost/action** | **Control**                                              |
|---------------------------|------------------------|-----------------|-------------------------|----------------------------------------------------------|
| Patient plan Q&A          | 3k input / 300 output  | Luna            | ~US\$0.001              | 20/month/patient soft quota; cache approved FAQ          |
| History/adherence summary | 6k input / 1k output   | Terra           | ~US\$0.024              | Generate on state change/consult prep, not every view    |
| Clinical note structure   | 8k input / 1.5k output | Terra           | ~US\$0.034              | One draft + one explicit regenerate                      |
| Plan draft                | 10k input / 2k output  | Terra           | ~US\$0.044              | Professional-only; deterministic validation; max context |
| Content package           | 5k input / 1.5k output | Terra           | ~US\$0.028              | Monthly themes, weekly batches, evidence reuse           |

At 20 professionals using 100 Terra-class actions each plus 600 patients using 20 Luna-class questions, rough AI spend is about US\$60–90 (R\$312–468) before overhead. That makes quotas, prompt compression, caching, and action-triggered generation necessary. Expose remaining usage clearly and stop at a hard monthly budget rather than creating a surprise invoice.

# **16. Business Model & Pricing Strategy**

## **Initial ICP**

The first commercial segment is a switcher, not a beginner: an independent Brazilian clinical nutritionist currently paying for WebDiet, Dietbox, Nutrium, or a similar product; approximately 20–150 active patients; dissatisfied with speed, stability, patient engagement, AI trust, or practice growth; willing to run a parallel test. Small clinics are a later expansion, not the MVP design center.

## **Launch offer**

| **Offer**           | **Price**                                | **Includes**                                                           | **Reason**                                                                   |
|---------------------|------------------------------------------|------------------------------------------------------------------------|------------------------------------------------------------------------------|
| 21-day switch trial | R\$0; no card                            | Supported import preview, 10 active patients, limited AI, patient PWA  | Two weeks is short for a real clinical cycle; import lowers evaluation risk. |
| Founding switcher   | R\$79/month for 12 months; first 30–50   | One professional, 75 active patients, 300 AI units, assisted migration | Fast learning and testimonials without a permanent free tier.                |
| Public Solo         | R\$99 monthly or R\$89 annual-equivalent | One professional, 75 active patients, core/growth, 300 AI units        | Sits near incumbent spend while selling migration + workflow value.          |
| Practice Growth     | R\$149 monthly post-MVP                  | 150 active, larger AI allowance, advanced growth/automation            | Monetizes commercial value and AI usage after validation.                    |
| Clinic              | From R\$299 + seats, post-MVP            | Roles, reception, shared care, governance/reporting                    | Do not absorb clinic complexity into the solo plan.                          |

AI unit should be a transparent task allowance, not opaque tokens. Never limit historical records: a patient limit tied to record deletion conflicts with continuity and retention obligations. Limit active care relationships and monthly AI usage instead. Patients pay nothing initially; premium patient subscriptions would create channel conflict and safety complexity.

## **Unit economics and break-even**

| **Paid solo accounts** | **MRR at R\$99** | **Operating estimate** | **Contribution before tax/support** |
|------------------------|------------------|------------------------|-------------------------------------|
| 10                     | R\$990           | R\$500–750             | R\$240–490                          |
| 20                     | R\$1,980         | R\$650–950             | R\$1,030–1,330                      |
| 50                     | R\$4,950         | R\$1,200–2,000         | R\$2,950–3,750                      |

These are planning estimates before payment fees, tax, founder labor, customer support, refunds, and legal/accounting costs. The important control is gross-margin telemetry by tenant and AI task from day one.

# **17. UX / Core User Journeys**

## **Acquisition journey**

| **Step** | **Experience**                                                                                                                                                        | **Success criterion**                                           |
|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------|
| Discover | Landing-page message names the switcher problem: migration risk, slow workflows, weak adherence, and difficulty growing a practice                                    | Relevant visitor self-qualifies without decoding a feature list |
| Trust    | See the closed care loop, migration promise, professional-controlled AI, co-founder practitioner credibility, privacy stance, and a short interactive product preview | Core objections answered before a sales conversation            |
| Convert  | Apply for early access or request a demo with current software, active-patient range, primary pain, and preferred contact channel                                     | Qualified submission in ≤2 minutes with explicit consent        |
| Qualify  | Founders review fit, schedule workflow interview/demo, and record acquisition source plus objection                                                                   | High-fit switchers receive a next step within one business day  |
| Activate | Prospect enters the supported switch trial and sees migration coverage before committing                                                                              | Landing lead → qualified demo → activated trial is measurable   |

## **Nutritionist switcher journey**

| **Step**      | **Experience**                                                                                                     | **Click/time reduction**                          |
|---------------|--------------------------------------------------------------------------------------------------------------------|---------------------------------------------------|
| 1\. Evaluate  | Arrive from the landing page; use an interactive demo with fake patient; compare migration coverage; export pledge | No signup before value proof                      |
| 2\. Import    | Upload supported CSV/PDF → map → preview → validate → import report                                                | One guided flow; reversible before commit         |
| 3\. Configure | Professional profile, positioning, plan defaults, brand voice, consent templates                                   | Progressive setup; defaults from import           |
| 4\. Prepare   | Agenda + pre-consult brief + missing intake items                                                                  | One consultation workspace; no tab hunting        |
| 5\. Consult   | Assessment, notes, measurements, goals with autosave/keyboard flow                                                 | No duplicate fields; shortcuts/templates          |
| 6\. Plan      | Clone template or draft → search food → substitutions → constraint check → approve/publish                         | Inline totals; contextual search; no modal chains |
| 7\. Monitor   | Attention queue: questions, missed check-ins, deviations, follow-ups                                               | Exceptions first; bulk low-risk actions           |
| 8\. Grow      | Monthly theme → weekly content → approve/export → lead arrives → follow-up task                                    | One repeatable weekly ritual                      |

## **Patient journey**

| **Step**   | **Experience**                                                           | **Success criterion**                                     |
|------------|--------------------------------------------------------------------------|-----------------------------------------------------------|
| Invite     | Branded secure link, identity setup, consent, notification preference    | Activation without support                                |
| Today      | Current meal/goal, next action, quick question                           | Relevant plan visible immediately                         |
| Follow     | One-tap followed/changed/skipped; optional photo/note                    | ≤10 seconds for simple check-in                           |
| Substitute | Contextual approved alternatives with equivalent portion and explanation | No leaving meal or searching a master list                |
| Ask        | Plan-grounded answer or escalation to nutritionist                       | No diagnosis or silent plan change                        |
| Progress   | Measurements/photos/goals and plain-language trend                       | Support motivation without shame or overclaim             |
| Follow-up  | Reminder and concise progress summary                                    | Patient and professional enter return with shared context |

## **UX principles**

- Task-centered navigation: Consult, Plan, Monitor, Grow—not a feature warehouse.

- Autosave with visible state and version history; undo where feasible; never silently discard clinical edits.

- Patient Today view is mobile-first, thumb-reachable, WCAG-aware, low-bandwidth tolerant, and pt-BR plain language.

- Internationalization keys, locale-aware units/dates, and food-source locales from day one; no translated strings in domain logic.

- AI draft and approved content look different; uncertainty and source links are visible; rejection is one click with a reason.

# **18. Engineering & Testing Strategy**

| **Layer**        | **What to test**                                                                                               | **Deployment gate**                                             |
|------------------|----------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------|
| Unit/domain      | Nutrient calculations, portions, plan state, roles, retention, scoring rules                                   | All changed-domain tests pass; deterministic rounding fixtures  |
| Integration      | PostgreSQL/Testcontainers, migrations, RLS/policies, outbox/jobs, signed files, auth JWT                       | No cross-tenant or migration regression                         |
| API/contract     | OpenAPI compatibility, validation, errors, idempotency, pagination                                             | Generated client builds; breaking change explicitly versioned   |
| E2E              | Landing lead capture; switcher import; consult→publish; patient check-in/substitution; export; privacy request | Critical journeys pass in production-like environment           |
| Security         | SAST/SCA/secrets; IDOR/BOLA; rate limits; file upload; MFA/session; prompt injection                           | No unresolved critical/high; medium risk accepted in writing    |
| Performance      | p95 API; food search; plan save; bulk import; concurrent check-ins                                             | Defined budgets met at 3× expected launch load                  |
| AI eval          | Schema, groundedness, constraints, unsafe output, prompt injection, edit/acceptance                            | No severe safety regression; score thresholds by task           |
| Migration/backup | Forward/backward migration rehearsal; restore DB + object references                                           | Restore succeeds within launch RTO/RPO; checksum reconciliation |
| Accessibility    | Keyboard, focus, screen reader, contrast, zoom, mobile touch targets                                           | Critical flows meet WCAG 2.2 AA target                          |

## **Production blockers**

Block deployment for failed critical E2E, cross-tenant access, data loss/corruption, invalid migration, critical/high security vulnerability, unreviewed schema change, broken restore, missing rollback, severe AI safety regression, missing consent/audit on a changed regulated flow, or monitoring/alerting not receiving a test signal.

# **19. Product & Technical Metrics**

| **North-star / metric**      | **Definition**                                                                              | **Initial target**                                                 |
|------------------------------|---------------------------------------------------------------------------------------------|--------------------------------------------------------------------|
| Weekly successful care loops | Patient receives plan/check-in → professional sees signal → professional action when needed | Establish baseline; ≥60% active patients/week by day 90            |
| Qualified landing conversion | Qualified early-access/demo submissions ÷ relevant landing visitors                         | Establish channel baseline; target ≥5% for warm co-founder traffic |
| Lead response time           | Qualified submission → founder contact or scheduled next step                               | ≤1 business day                                                    |
| Demo-to-pilot conversion     | Qualified demos that enter a supported pilot                                                | ≥30% during founder-led validation                                 |
| Nutritionist activation      | Imported/created patient + published plan + patient activated within 7 days                 | ≥60% of qualified trials                                           |
| Time to first patient        | Signup → usable patient record                                                              | Median \<30 min for supported switch import                        |
| Time to first plan           | Signup → first published plan                                                               | Median \<48h; in-workflow build \<15 min                           |
| Patient activation           | Invite → consent/account → plan viewed                                                      | ≥75% within 72h                                                    |
| Adherence engagement         | Active patients with ≥1 intended weekly check-in                                            | ≥60%, segmented by protocol                                        |
| Trial conversion             | Qualified trial → paid                                                                      | ≥20% initially; ≥35% after onboarding iteration                    |
| Nutritionist retention       | Logo retention / 90-day                                                                     | \>85% for activated accounts                                       |
| AI acceptance                | Draft accepted with minor edits / reviewed outputs                                          | Track by task; \>60% only after safety threshold                   |
| AI safety                    | Severe constraint/clinical-policy violations                                                | 0 patient-exposed severe incidents                                 |
| Availability                 | Successful synthetic/API checks                                                             | MVP target 99.5%; error budget reviewed                            |
| Latency/error                | p95 read/write; 5xx                                                                         | p95 reads \<500ms, writes \<800ms; 5xx \<1%                        |
| Jobs                         | Failed/stuck job rate                                                                       | \<0.5%; no stuck critical jobs \>15 min                            |
| AI latency/cost              | p95 response; spend by task/tenant                                                          | Professional draft p95 \<20s; budget hard cap                      |
| DB/backup                    | Connections, slow queries, restore success                                                  | No pool exhaustion; quarterly restore pass                         |

# **20. Architecture Decision Records**

## **ADR-001 — Frontend framework**

| **Context**      | Multiple web clients and future mobile; solo React/TS expertise.                                 |
|------------------|--------------------------------------------------------------------------------------------------|
| **Options**      | React SPA, Next.js, Remix.                                                                       |
| **Decision**     | Next.js App Router + TypeScript.                                                                 |
| **Why**          | Strong full-stack web DX, routing, PWA path, previews; no requirement that backend live in Next. |
| **Trade-offs**   | Framework/server features can increase Vercel coupling; keep API authoritative.                  |
| **Revisit when** | SSR/hosting economics or framework stability materially changes.                                 |

## **ADR-002 — Backend**

| **Context**      | Clinical transactions, security, AI jobs, and founder Java expertise.                                        |
|------------------|--------------------------------------------------------------------------------------------------------------|
| **Options**      | Spring Boot, NestJS, Go.                                                                                     |
| **Decision**     | Java 25 LTS + Spring Boot 4.1 modular monolith.                                                              |
| **Why**          | Highest founder leverage; mature validation/security/transactions/observability; Spring Modulith boundaries. |
| **Trade-offs**   | Two language ecosystems; more memory than Node.                                                              |
| **Revisit when** | Team composition changes or measured compute economics dominate developer productivity.                      |

## **ADR-003 — Database**

| **Context**      | Relational clinical records, versions, tenancy, audit, food data.               |
|------------------|---------------------------------------------------------------------------------|
| **Options**      | PostgreSQL, MySQL, MongoDB.                                                     |
| **Decision**     | PostgreSQL.                                                                     |
| **Why**          | Constraints, transactions, JSON/search extensions, portability, mature tooling. |
| **Trade-offs**   | Schema discipline and migrations required.                                      |
| **Revisit when** | A specialized workload has measured needs PostgreSQL cannot meet.               |

## **ADR-004 — Cloud/provider**

| **Context**      | R\$1,000/month ceiling, solo ops, Brazil-first latency/data location.                       |
|------------------|---------------------------------------------------------------------------------------------|
| **Options**      | AWS-only, GCP-only, Railway/Render, Vercel+Fly+Supabase.                                    |
| **Decision**     | Vercel Pro + Fly.io GRU + Supabase São Paulo.                                               |
| **Why**          | Best early DX/cost/region balance with managed data plane.                                  |
| **Trade-offs**   | Several vendors and public TLS DB path; no single SLA.                                      |
| **Revisit when** | Enterprise procurement, private networking, uptime, or R\$10k+ MRR justifies consolidation. |

## **ADR-005 — Authentication**

| **Context**      | Web/mobile identity, MFA, recovery, invitations.                       |
|------------------|------------------------------------------------------------------------|
| **Options**      | Build it, Keycloak, Auth0/Clerk, Cognito, Supabase Auth.               |
| **Decision**     | Supabase Auth; TOTP for professionals; own authorization domain.       |
| **Why**          | Low operational burden and regional project option; supports PKCE/MFA. |
| **Trade-offs**   | Provider dependency; recovery limitations must be designed.            |
| **Revisit when** | Enterprise SSO/SCIM or cost/security requirements exceed offering.     |

## **ADR-006 — API**

| **Context**      | Multiple first-party clients and future integrations.                                        |
|------------------|----------------------------------------------------------------------------------------------|
| **Options**      | REST, GraphQL, tRPC.                                                                         |
| **Decision**     | REST + OpenAPI; generated clients; webhook later.                                            |
| **Why**          | Clear contracts, caching/idempotency, mobile and third-party friendly.                       |
| **Trade-offs**   | Potential over/under-fetching; version discipline.                                           |
| **Revisit when** | Client composition becomes genuinely dynamic and GraphQL benefits exceed authorization cost. |

## **ADR-007 — Multi-tenancy**

| **Context**      | Solo practices now; clinics later; low cost.                                     |
|------------------|----------------------------------------------------------------------------------|
| **Options**      | Shared schema, per-schema, per-database.                                         |
| **Decision**     | Shared DB/shared schema with organization_id + policy tests + RLS defense.       |
| **Why**          | Fastest/cheapest; easy migration and analytics.                                  |
| **Trade-offs**   | Highest blast radius if authorization is wrong.                                  |
| **Revisit when** | Enterprise isolation/residency or noisy-neighbor evidence requires dedicated DB. |

## **ADR-008 — Mobile**

| **Context**      | Patient mobile is strategic but not in 16-week MVP.                     |
|------------------|-------------------------------------------------------------------------|
| **Options**      | PWA, Expo/RN, Flutter, native.                                          |
| **Decision**     | Responsive PWA MVP; Expo/React Native post-MVP.                         |
| **Why**          | Validates journeys now; later shares TS client/schemas/tokens with web. |
| **Trade-offs**   | PWA push/offline/store presence limitations.                            |
| **Revisit when** | Patient retention proves native value and team can support releases.    |

## **ADR-009 — AI abstraction**

| **Context**      | Models/prices/safety change; task-specific routing needed.                     |
|------------------|--------------------------------------------------------------------------------|
| **Options**      | Direct SDK, multi-provider from day 1, internal gateway.                       |
| **Decision**     | One provider behind AiGateway + task registry/model snapshots.                 |
| **Why**          | Avoids premature multi-provider complexity while containing coupling.          |
| **Trade-offs**   | Gateway and evaluation infrastructure add work.                                |
| **Revisit when** | Provider incident, compliance, quality, or economics requires second provider. |

## **ADR-010 — Background processing**

| **Context**      | Imports, exports, reminders, AI, emails must be reliable.                         |
|------------------|-----------------------------------------------------------------------------------|
| **Options**      | Inline, Postgres jobs/outbox, Redis, managed queue/Kafka.                         |
| **Decision**     | Transactional outbox + JobRunr/Postgres.                                          |
| **Why**          | Atomic with domain data and zero new infrastructure.                              |
| **Trade-offs**   | DB polling/throughput ceiling.                                                    |
| **Revisit when** | Sustained queue load, independent scaling, or blast-radius isolation is measured. |

## **ADR-011 — File storage**

| **Context**      | Photos, PDFs, imports, exports; sensitive/private.                                   |
|------------------|--------------------------------------------------------------------------------------|
| **Options**      | DB BLOB, local disk, object storage.                                                 |
| **Decision**     | Private managed object storage with signed URLs and metadata in PostgreSQL.          |
| **Why**          | Scalable, lifecycle-friendly, client upload; DB stays lean.                          |
| **Trade-offs**   | Backup/restore spans systems; URL policy risk.                                       |
| **Revisit when** | Volume/compliance needs dedicated S3 account, immutability, or regional replication. |

# **21. Risk Register**

| **Risk**                                               | **Probability** | **Impact** | **Mitigation**                                                                                                                                                                                |
|--------------------------------------------------------|-----------------|------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| MVP too broad for one developer                        | High            | Critical   | Developer owns delivery; nutritionist co-founder owns clinical workflow and pilot engagement; freeze must-haves; weekly scope burn; growth is lite; production at week 16 over feature count. |
| Embedded co-founder evidence creates confirmation bias | Medium          | High       | Use the nutritionist co-founder as Design Partner Zero, but validate willingness to switch/pay with at least five external nutritionists and record disconfirming evidence.                   |
| Fewer than five external pilots produce weak evidence  | High            | High       | Co-founder recruits 8–12 external interview prospects in Sprint 0 to retain 5 active pilots; target switcher communities and churn signals.                                                   |
| Migration formats unavailable/inconsistent             | High            | High       | Start with one supported CSV template + document archive; mapping preview; concierge; do not promise universal import.                                                                        |
| Food data license/quality issue                        | Medium          | Critical   | TACO legal confirmation; visible provenance; TBCA written permission; source/version tests; custom foods.                                                                                     |
| Cross-tenant data exposure                             | Medium          | Critical   | Organization-scoped repositories, relationship policies, RLS, negative integration tests, security review, audit/alerts.                                                                      |
| AI unsafe/hallucinated advice                          | High            | Critical   | Deterministic rules, grounding, structured output, approval gates, refusal/escalation, eval/red-team, kill switch.                                                                            |
| Prompt injection/data exfiltration                     | Medium          | Critical   | Untrusted-content boundaries, no open tools, allowlists, output filters, minimal data, adversarial tests.                                                                                     |
| LGPD/CFN non-compliance                                | Medium          | Critical   | Counsel before beta; DPA/data map/RIPD decision; consent/retention/incident workflow; CFN marketing/telenutrition review.                                                                     |
| Provider outage or lock-in                             | Medium          | High       | Postgres/OpenAPI/OIDC/storage adapters; backups/export; containers; provider status alerts; recovery playbook.                                                                                |
| Cost exceeds R\$1,000                                  | Medium          | High       | Hard AI/provider budgets, alerts at 50/75/90%, quotas, no PITR initially, free telemetry tiers, monthly unit economics.                                                                       |
| Incumbents copy AI/growth features                     | High            | Medium     | Compete on integrated workflow, migration, UX reliability, data feedback, governance, and customer intimacy.                                                                                  |
| Patient engagement remains low                         | Medium          | High       | Today-first design; configurable low-friction check-ins; onboarding tests; notification consent/cadence; measure activation.                                                                  |
| Nutritionists reject AI as commoditizing profession    | Medium          | High       | Professional-control positioning, labels, audit, edit/approval, explain value as time reclaimed and better continuity.                                                                        |
| Social content violates ethics                         | Medium          | High       | Ruleset/checklist, approval, evidence, forbidden-pattern tests, no auto-post; professional remains responsible.                                                                               |
| Solo-developer support overload                        | High            | High       | Nutritionist co-founder owns structured pilot communication and triage; narrow supported migration, in-product diagnostics, admin tools, runbooks, office hours, capped founding cohort.      |
| Retention/deletion conflict                            | Medium          | High       | Purpose-based schedule, legal holds, deactivation vs deletion, export, counsel-validated workflow, audit.                                                                                     |

# **22. Sprint-by-Sprint Implementation Plan**

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>Critical path</strong></p>
<p>Co-founder workflow capture + external pilot recruitment → landing-page deployment and lead capture → migration contract/sample exports → tenancy/security foundation → food provenance and plan builder → patient publication/check-in → real pilot feedback → LGPD/CFN/legal validation → restore/security/AI gates → paid production.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

## **Sprint 0 (weeks 1–2) — Discovery & Technical Foundation**

| **Objective**           | Discovery & Technical Foundation                                                                                                                                                                                                                                                                                                                 |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Features**            | Use the nutritionist co-founder as Design Partner Zero; interview at least 5 external switchers and recruit 8–12 prospects; map WebDiet/current exports and plan creation; deploy a pt-BR landing page with early-access capture; ship testable import, plan-builder, Today, and Growth slices using realistic fake data; define beta agreement. |
| **Technical work**      | Monorepo/repositories; Java/Spring and Next deployable skeletons; continuous deployment from day one; preview/staging environments; landing form + consent + funnel events; vertical-slice feature flags; ADR baseline; data map/threat model; OpenAPI; telemetry; Supabase/Fly/Vercel setup; TACO/TBCA legal inquiry.                           |
| **Deliverables**        | Live landing page; working deployment pipeline; authenticated product shell; at least one end-to-end mocked workflow; validated problem brief; ranked JTBD; usability findings; supported-import contract; architecture runway; measurement plan; external pilot roster.                                                                         |
| **Acceptance criteria** | First public deployment by day 3–5; every main-branch change deploys automatically; landing lead capture/consent/events work; co-founder completes the mocked workflow; ≥5 external interviews including ≥3 active incumbent users; ≥3 external prospects agree to pilot; no unresolved architecture blocker; scope signed off.                  |
| **Dependencies**        | Co-founder availability; access to external nutritionists; de-identified sample exports; legal/privacy consultation scheduling.                                                                                                                                                                                                                  |
| **Complexity**          | XL                                                                                                                                                                                                                                                                                                                                               |

## **Sprint 1 (weeks 3–4) — Identity, Tenancy & Switcher Onboarding**

| **Objective**           | Identity, Tenancy & Switcher Onboarding                                                                                                                                          |
|-------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Features**            | Professional signup/profile; organization; patient invitation shell; CSV import mapping/preview/errors; document archive upload; export skeleton.                                |
| **Technical work**      | Supabase Auth/PKCE/TOTP; membership/RBAC/relationship policies; tenant-scoped repositories/RLS; Flyway; signed file uploads; audit; import job/outbox; fake-data admin.          |
| **Deliverables**        | Deployed staging; import supported template; tenant-security test suite; onboarding analytics.                                                                                   |
| **Acceptance criteria** | Two organizations cannot access each other by API/object URL; import is idempotent/reversible pre-commit; error report identifies every skipped row; MFA works for professional. |
| **Dependencies**        | Sprint 0 import contract; identity/provider account; DPA draft.                                                                                                                  |
| **Complexity**          | XL                                                                                                                                                                               |

## **Sprint 2 (weeks 5–6) — Patient Record, Intake & Consultation**

| **Objective**           | Patient Record, Intake & Consultation                                                                                                                                                       |
|-------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Features**            | Patient profile/care relationship; consent; intake/anamnesis; core clinical history; consultation; typed notes; goals; core measurements/photos.                                            |
| **Technical work**      | Versioned clinical notes; private media metadata; validation; autosave; audit; privacy-request/export data model; accessibility baseline.                                                   |
| **Deliverables**        | End-to-end create/import patient → intake → consultation flow; tested prototypes with pilots.                                                                                               |
| **Acceptance criteria** | Autosave survives refresh; final note immutable with amendment; consent evidence versioned; imported and newly created patients behave consistently; zero critical pilot usability defects. |
| **Dependencies**        | Tenancy/auth; consent/legal wording; pilot availability.                                                                                                                                    |
| **Complexity**          | XL                                                                                                                                                                                          |

## **Sprint 3 (weeks 7–8) — Food Data & Fast Plan Builder**

| **Objective**           | Food Data & Fast Plan Builder                                                                                                                                              |
|-------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Features**            | TACO food search/provenance; custom foods; meals/items/portions; nutrient totals; template clone; substitutions; plan draft/review/publish/supersede; PDF.                 |
| **Technical work**      | Food ingestion/normalization tests; deterministic calculators/rounding; plan aggregate/versioning; optimistic locking/autosave; search indexes; constraint engine v1.      |
| **Deliverables**        | Professional can build and publish a standard plan; patient-ready API; calculation reference fixtures.                                                                     |
| **Acceptance criteria** | Reference totals match fixtures; provenance visible; concurrent edit conflict is explicit; patient never sees draft; trained median plan build ≤15 min in target scenario. |
| **Dependencies**        | Food legal decision; consultation/patient model; UX tests.                                                                                                                 |
| **Complexity**          | XL — highest delivery risk                                                                                                                                                 |

## **Sprint 4 (weeks 9–10) — Patient PWA & Adherence Loop**

| **Objective**           | Patient PWA & Adherence Loop                                                                                                                                                |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Features**            | Invite/onboarding; Today; plan/recipe-context placeholder; approved substitutions; one-tap check-in; measurements/photos; question inbox; email reminders; progress graphs. |
| **Technical work**      | Mobile-responsive PWA; notification preference; engagement events; attention rules; signed media; rate limits; synthetic patient journey; sanitized analytics.              |
| **Deliverables**        | Real pilot patient can receive plan, follow/check in, substitute, ask, and see progress; nutritionist sees attention queue.                                                 |
| **Acceptance criteria** | Patient simple check-in ≤10 sec; no cross-patient access; reminders honor preference; attention flag contains explainable evidence; WCAG critical-flow review passes.       |
| **Dependencies**        | Published plan API; email domain; pilot patients/consent.                                                                                                                   |
| **Complexity**          | XL                                                                                                                                                                          |

## **Sprint 5 (weeks 11–12) — AI Copilot & Private Beta**

| **Objective**           | AI Copilot & Private Beta                                                                                                                                                          |
|-------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Features**            | Pre-consult summary; typed notes → structured draft; professional plan draft; adherence digest; plan-grounded patient Q&A behind flag; approval/rejection; usage display.          |
| **Technical work**      | AiGateway/task registry; store:false; structured outputs; prompt/policy/model versions; PHI minimization; cost/latency telemetry; eval harness; injection defenses; kill switches. |
| **Deliverables**        | Private beta release; AI evaluation report; review/audit UI; per-tenant budget controls.                                                                                           |
| **Acceptance criteria** | Schema-valid ≥99% on eval; zero patient-exposed severe policy violations in golden/red-team set; every output traceable; budget cap works; beta legal gates signed.                |
| **Dependencies**        | Core data loop; AI DPA/transfer review; nutritionist evaluators.                                                                                                                   |
| **Complexity**          | XL                                                                                                                                                                                 |

## **Sprint 6 (weeks 13–14) — Practice Growth Lite & Commercial Onboarding**

| **Objective**           | Practice Growth Lite & Commercial Onboarding                                                                                                                                                                                                                    |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Features**            | Refine the validated acquisition landing page; positioning brief; monthly/weekly content plan; caption/video outline drafts; evidence/ethics checklist; approval/export; public professional profile + lead form; follow-up task; founding plan administration. |
| **Technical work**      | Separate growth/lead domain; anti-spam/rate limit; content prompt/eval; prohibited-pattern checks; conversion events; secure lead-to-patient conversion; hosted payment link/manual entitlement.                                                                |
| **Deliverables**        | One complete growth ritual and lead pipeline; pricing/trial screen; switcher onboarding guide.                                                                                                                                                                  |
| **Acceptance criteria** | No draft auto-publishes; forbidden fee/promotion/before-after scenarios flagged; lead data separated from clinical record; ≥3 pilots complete weekly planning; commercial entitlement auditable.                                                                |
| **Dependencies**        | CFN marketing review; AI foundation; landing funnel evidence and profile copy.                                                                                                                                                                                  |
| **Complexity**          | L                                                                                                                                                                                                                                                               |

## **Sprint 7 (weeks 15–16) — Hardening, Migration Pilot & Production Launch**

| **Objective**           | Hardening, Migration Pilot & Production Launch                                                                                                                                                         |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Features**            | Pilot fixes; migration concierge; privacy center; admin/support tools; status/help; launch onboarding; feedback and cancellation/export.                                                               |
| **Technical work**      | Load/security/accessibility tests; backup + object restore drill; incident tabletop; SLO/alerts; log scrub; dependency scan; rollback; production checklist; runbooks; cost alarms.                    |
| **Deliverables**        | Production release; first paid switchers; launch report; prioritized post-MVP backlog.                                                                                                                 |
| **Acceptance criteria** | All production blockers clear; RPO/RTO drill passes; no unresolved critical/high security findings; ≥3 real practices migrated; ≥2 paid/committed switchers; costs project ≤R\$1,000; rollback tested. |
| **Dependencies**        | All prior sprints; counsel sign-off; pilot availability; provider production accounts.                                                                                                                 |
| **Complexity**          | XL                                                                                                                                                                                                     |

## **Scope contingency**

If the plan builder or patient loop slips, cut in this order: public professional profile → content video variants → patient AI Q&A → progress photos → document batch convenience. Keep the lightweight acquisition landing page and lead capture, because they are required to recruit and convert pilots. Do not cut tenant isolation, exports, consent/audit, plan versioning, deterministic calculations, patient Today/check-in, backups, or release gates.

# **23. MVP Launch Checklist**

## **Product and pilots**

- [ ] Acquisition landing page is live in pt-BR; early-access/demo capture, consent, source attribution, qualification, and follow-up analytics work.

- [ ] At least three switcher practices migrated and using the full loop with real patients.

- [ ] Activation, time-to-plan, patient activation, adherence, and cancellation/export events visible.

- [ ] MVP scope/help/pricing/trial/support boundaries published in pt-BR.

- [ ] No critical pilot issue without owner, workaround, and deadline.

## **Clinical and AI safety**

- [ ] Nutritionist approval gates verified; draft/approved labels visible; no autonomous plan modification.

- [ ] Constraint/calculation fixtures and AI evaluation thresholds pass; kill switches tested.

- [ ] Patient escalation and emergency disclaimer reviewed; no open-web clinical answering.

## **Privacy, legal, and professional rules**

- [ ] Terms, privacy notice, DPA, subprocessors, data-flow map, lawful-basis/retention schedule reviewed by counsel.

- [ ] Consent, guardian path decision, privacy requests, export, withdrawal, deletion/legal-hold flows tested.

- [ ] CFN/CRN review of telenutrition boundary, professional documents, marketing assistant, and record retention complete.

- [ ] International-transfer mechanism and AI/provider configurations documented.

## **Security and reliability**

- [ ] Threat model closed; tenant/IDOR/object tests pass; MFA and recovery verified.

- [ ] Private files, signed URLs, upload validation/scanning, secrets, dependency/container scans pass.

- [ ] Daily backups, object inventory, restore, rollback, incident tabletop, alerts, and status communication tested.

- [ ] Logs/analytics/AI telemetry sampled and confirmed free of clinical payloads/secrets.

## **Operations and economics**

- [ ] Provider and AI hard budgets/alerts configured; forecast ≤R\$1,000 for first six months.

- [ ] Support/admin/runbooks, incident contacts, migration playbook, cancellation/export procedure ready.

- [ ] Hosted billing/manual entitlements, invoice/refund/tax/accounting process ready.

- [ ] Founder on-call capacity and founding-cohort cap explicitly set.

# **24. Open Questions / Decisions Required**

| **Decision**               | **Recommended default**                                                                                                                                                               | **Deadline / consequence**                                                                                 |
|----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| Exact switcher segment     | Independent clinical nutritionists with 20–150 active patients                                                                                                                        | Sprint 0; changes protocols, acquisition, and pricing                                                      |
| Founder roles              | Igor: product/engineering/architecture/operations. Nutritionist co-founder: clinical product ownership, WebDiet workflow mapping, pilot recruitment/interviews, safety/content review | Day 1; prevents implicit ownership and solo-developer overload                                             |
| Pilot recruitment          | Treat co-founder as Design Partner Zero; recruit 8–12 external prospects to retain ≥5 active; at least 3 current incumbent users                                                      | Week 1; co-founder insight cannot substitute for external willingness-to-switch evidence                   |
| Landing conversion promise | Lead with safe switching + faster care loop + adherence + ethical growth; one primary early-access/demo CTA                                                                           | Day 2; determines landing copy, qualification fields, and funnel events                                    |
| Supported migration source | Start with a canonical CSV + document archive; add one competitor export only after samples                                                                                           | Week 2; defines onboarding promise                                                                         |
| Food data permission       | Use TACO only after confirmation; request written TBCA commercial/bulk permission                                                                                                     | Before Sprint 3; can block plan builder                                                                    |
| Telenutrition scope        | Do not include video; record remote modality/consent if appointments represent telenutrition                                                                                          | Before beta; legal/CFN workflow impact                                                                     |
| Children/adolescents       | Exclude under-18s from beta unless guardian/consent/clinical workflow is designed and reviewed                                                                                        | Before beta; material LGPD/clinical risk                                                                   |
| AI patient assistant       | Feature-flag controlled beta; approved plan only; no autonomous modifications                                                                                                         | Sprint 5 gate; can be cut without breaking core MVP                                                        |
| Social-growth claims       | CFN-reviewed rule pack; no auto-post, fees/promotions, before-after, guarantees, or brand endorsements                                                                                | Before Sprint 6 pilot                                                                                      |
| Data roles and retention   | Nutritionist controller / SaaS operator for care data; purpose-based separate controller roles                                                                                        | Counsel before real data                                                                                   |
| Brand/name/domain          | Working recommendation: Vincelia; fallback: Ritmera. Finalize only after pronunciation/recall testing and INPI clearance.                                                             | Week 1 validation gate; register the Brazilian and international-path domains immediately after selection. |

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>Immediate next action</strong></p>
<p>Run discovery and delivery in parallel. In days 1–5, deploy the pt-BR landing page, lead capture, continuous-delivery pipeline, authenticated product shell, and one mocked migration → plan → patient-Today vertical slice. At the same time, map the nutritionist co-founder’s complete WebDiet workflow, obtain de-identified export samples from at least two external switchers, begin external interviews, and book Brazilian privacy + CFN-specialist review. Build thin testable slices immediately; do not commit deeply to the production plan-builder model before the workflow and migration evidence arrive.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

# **Appendix A — Brand, Domain & Interface-System Direction**

*Research status: commercial and product screening completed 18 August 2026. This is not legal clearance. Domain availability is transactional and must be rechecked at purchase.*

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>Conditional working decision</strong></p>
<p>Proceed with Vincelia as the working masterbrand and retain Ritmera as the fallback. Do not commission a permanent logo or publicly announce either name until the founder test and trademark gate below are complete.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

## **A.1 Naming decision**

| **Candidate**          | **Strategic assessment**                                                                                                                                        | **Domain path and risk**                                                                                                                                                 |
|------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Vincelia — recommended | Suggests vínculo, continuity, and professional–patient connection. Broad enough for clinical workflow, practice growth, patient engagement, and future clinics. | Observed with no RDAP registration record: vincelia.com.br, usevincelia.com, getvincelia.com. Exact .com is registered. Test pronunciation and spelling before purchase. |
| Ritmera — fallback     | Suggests rhythm, routine, adherence, and a modern operating cadence. Stronger patient-habit story, but weaker immediate connection to professional practice.    | Observed with no RDAP registration record: ritmera.com.br, useritmera.com, getritmera.com. Requires a stricter dictation/spelling test.                                  |

**Why the masterbrand should not begin with Nutri-, Diet-, Care-, Clinic-, Vita-, Well-, or AI:**

- Those territories are heavily saturated by nutrition, wellness, supplement, and healthcare products, weakening distinctiveness and searchability.

- A meal-plan name would underrepresent the practice-growth, lead, communication, adherence, and clinic roadmap.

- AI is a capability with approval and safety boundaries—not the company identity or an autonomous substitute for professional judgment.

## **A.2 Masterbrand and product architecture**

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>One brand in the MVP</strong></p>
<p>Use one masterbrand across the company, nutritionist workspace, patient PWA, and eventual mobile app. Growth and Copilot are descriptive modules, not separate brands. Add “For Clinics” only when team/organization functionality exists.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

| **Layer**               | **MVP naming** | **Future rule**                                                                           |
|-------------------------|----------------|-------------------------------------------------------------------------------------------|
| Company + product       | Vincelia       | Keep the same masterbrand internationally where clearance permits.                        |
| Nutritionist experience | Vincelia       | Role-adaptive workspace; no “Pro” sub-brand needed initially.                             |
| Patient experience      | Vincelia       | Same app name; role and invitation determine the interface.                               |
| Practice acquisition    | Growth         | Navigation module covering weekly planning, content, leads, and follow-ups.               |
| AI assistance           | Copilot        | Contextual capability with visible draft/approval labels; never the primary destination.  |
| Organizations           | For Clinics    | Commercial edition only after staff, permissions, and multi-professional workflows exist. |

## **A.3 Domain and application topology**

Recommended initial registrations: vincelia.com.br as the Brazil-first canonical domain and usevincelia.com as the international-path defensive domain. Avoid buying a large defensive portfolio before validation.

| **Address**                            | **Purpose**                                                                                          | **MVP timing**                       |
|----------------------------------------|------------------------------------------------------------------------------------------------------|--------------------------------------|
| vincelia.com.br                        | Canonical public landing page, pricing, resources, legal pages, and future professional profiles.    | Day 1 after selection                |
| app.vincelia.com.br                    | Authenticated nutritionist and patient web/PWA surface.                                              | Sprint 0                             |
| api.vincelia.com.br                    | Versioned backend API consumed by web, future mobile, and approved integrations.                     | Sprint 0                             |
| vincelia.com.br/profissionais/{handle} | Future indexable professional profile and lead form. Prefer a path over per-professional subdomains. | Post-MVP or validated Sprint 6 slice |
| ajuda.vincelia.com.br                  | Help centre and onboarding knowledge base.                                                           | Post-MVP                             |
| status.vincelia.com.br                 | Public incident and availability communication.                                                      | Before broader PMF scale             |

**Operational rules**

- Centralize DNS and TLS management; redirect the international-path domain to the canonical locale until an international site exists.

- Keep API endpoints out of public navigation and expose only documented, authenticated capabilities.

- Use path-based professional profiles initially for simpler SEO, analytics, routing, moderation, and certificate management.

- Reserve consistent email identities such as oi@, suporte@, privacidade@, and seguranca@ on the canonical domain.

## **A.4 Brand-informed interface layout**

| **Surface**        | **Primary layout**                                                                                                                                | **Brand behavior**                                                                                                 |
|--------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------|
| Public landing     | Editorial sections: promise → switcher proof → workflow preview → patient experience → growth support → founding offer → FAQ → early-access form. | Warm and aspirational, but evidence-led. One primary CTA and real product screenshots as soon as available.        |
| Nutritionist web   | Desktop-first left rail: Today, Patients, Plans, Calendar, Growth, Messages, Insights. Global search/command access; contextual detail panels.    | Calm, fast, information-dense. Brand colour guides orientation; clinical status uses independent semantic colours. |
| Patient PWA/mobile | Bottom navigation: Today, Plan, Track, Progress, Messages. One dominant action per screen and nutritionist identity kept visible.                 | Supportive and reassuring. Reduce administration, dense tables, and professional terminology.                      |
| Growth workspace   | Weekly/monthly planner with content pipeline, lead inbox, follow-up tasks, and evidence/ethics checks.                                            | Use a warmer accent for momentum without visually separating Growth into another product.                          |
| AI assistance      | Inline suggestions and a contextual side sheet inside patient, plan, consultation, and content workflows.                                         | Always label generated content and approval state. No omnipresent chatbot as the product shell.                    |

## **A.5 Design-system starting point**

Brand character: calm professional competence + human connection + forward movement. Explore a compact V/link/path symbol; avoid leaves, apples, plates, scales, measuring tapes, medical crosses, and sparkle-first AI marks.

| **Token role** | **Starting value**                          | **Use**                                                         |
|----------------|---------------------------------------------|-----------------------------------------------------------------|
| Brand dark     | \#173F3B                                    | Navigation, strong headings, premium trust moments              |
| Brand primary  | \#267565                                    | Primary actions, active states, links after contrast validation |
| Human accent   | \#E99763                                    | Growth/content emphasis and selective warmth                    |
| Canvas         | \#F7F5EF                                    | Warm neutral backgrounds and landing-page sections              |
| Primary text   | \#17201E                                    | Long-form and application content                               |
| AI accent      | Muted violet; finalize after contrast tests | Generated/draft states only; never warnings or approval         |

Implementation rule: brand tokens and semantic tokens must remain separate. Success, warning, destructive, informational, AI-generated, and awaiting-professional-approval states may not reuse colour alone as their only signal.

## **A.6 Landing-page message direction**

<table>
<colgroup>
<col style="width: 100%" />
</colgroup>
<thead>
<tr class="header">
<th><p><strong>pt-BR working hero</strong></p>
<p>Menos tempo montando planos. Mais tempo cuidando — e crescendo.<br />
<br />
Gestão, acompanhamento de pacientes e ferramentas para fortalecer sua presença profissional em um só lugar.<br />
<br />
Primary CTA: Quero participar do acesso antecipado. Secondary CTA: Ver como funciona.</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

The landing page should sell the switch outcome—safer migration, faster workflows, a better patient experience, adherence visibility, and ethical practice growth—not present the full roadmap as a feature catalogue.

## **A.7 Decision gate before public commitment**

- [ ] Blind-test Vincelia and Ritmera with the nutritionist co-founder plus at least five external nutritionists: pronunciation, dictation, 24-hour recall, associations, and trust.

- [ ] Reject any candidate that fewer than 80% can write correctly after hearing once, or that creates repeated supplement, clinic, or AI-diet associations.

- [ ] Run exact, phonetic, and visually similar searches in INPI; have specialist counsel validate the relevant Nice classes, expected to include software/SaaS and potentially professional-service adjacency.

- [ ] Search WIPO and the priority expansion markets; absence from a database is not proof of registrability.

- [ ] Recheck domains and key social handles, register them on the same day, and only then finalize the logo and permanent design tokens.

# **Appendix B — Principal Sources and Research Notes**

*Research performed 18 August 2026. Official sources are preferred. App-store/community evidence is observational. Pricing may change and should be re-checked before launch.*

**Source:** [WebDiet — product](https://webdiet.com.br/)

**Source:** [WebDiet — pricing](https://webdiet.co/assine.php?tg=true)

**Source:** [WebDiet — privacy](https://webdiet.com.br/privacidade/)

**Source:** [WebDiet patient app](https://play.google.com/store/apps/details?hl=pt_BR&id=br.com.webdiet.webdiet)

**Source:** [Dietbox — product/pricing](https://dietbox.me/pt-BR)

**Source:** [Dietbox patient app](https://play.google.com/store/apps/details?hl=pt_BR&id=com.craftbox.dietbox)

**Source:** [Dietbox professional App Store reviews](https://apps.apple.com/br/app/dietbox-para-profissionais/id1089653522?platform=ipad&see-all=reviews)

**Source:** [Nutrium](https://nutrium.com/en)

**Source:** [Nutrium client export](https://help.nutrium.com/en/articles/5086469-can-i-export-my-clients-data)

**Source:** [NutriAssist](https://www.nutriassist.com.br/)

**Source:** [Salutes](https://salutes.com.br/)

**Source:** [Healthie pricing](https://www.gethealthie.com/healthie-pricing)

**Source:** [Practice Better pricing](https://practicebetter.io/pricing)

**Source:** [That Clean Life pricing](https://thatcleanlife.com/pricing)

**Source:** [NutriAdmin pricing](https://nutriadmin.com/pricing)

**Source:** [TACO](https://cfn.org.br/wp-content/uploads/2017/03/taco_4_edicao_ampliada_e_revisada.pdf)

**Source:** [TBCA](https://www.tbca.net.br/)

**Source:** [LGPD](https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709.htm)

**Source:** [ANPD incidents](https://www.gov.br/anpd/pt-br/assuntos/comunicacao-de-incidentes-de-seguranca-cis)

**Source:** [ANPD international transfer](https://www.gov.br/anpd/pt-br/assuntos/assuntos-internacionais/transferencia-internacional-de-dados)

**Source:** [CFN ethics](https://cfn.org.br/wp-content/uploads/resolucoes/Res_599_2018.html)

**Source:** [CFN telenutrition](https://cfn.org.br/cfn-publica-resolucao-que-regulamenta-a-telenutricao/)

**Source:** [CFN records](https://cfn.org.br/wp-content/uploads/resolucoes/resolucoes_old/Res_594_2017.htm)

**Source:** [OpenAI models](https://developers.openai.com/api/docs/models)

**Source:** [OpenAI data controls](https://developers.openai.com/api/docs/guides/your-data)

**Source:** [Supabase pricing](https://supabase.com/pricing)

**Source:** [Supabase regions](https://supabase.com/docs/guides/platform/regions)

**Source:** [Fly.io regions](https://fly.io/docs/reference/regions/)

**Source:** [Fly.io pricing](https://fly.io/docs/about/pricing/)

**Source:** [Vercel pricing](https://vercel.com/pricing)

**Source:** [PostHog pricing](https://posthog.com/pricing)

**Source:** [Registro.br — new domain registration guidance](https://registro.br/ajuda/registro-de-novos-dominios/)

**Source:** [Registro.br — official WHOIS](https://registro.br/tecnologia/ferramentas/whois/)

**Source:** [INPI — trademark guide](https://www.gov.br/inpi/pt-br/servicos/marcas/guia-basico)

**Source:** [INPI — Trademark Manual](https://manualdemarcas.inpi.gov.br/)

**Source:** [WIPO — Global Brand Database](https://www.wipo.int/en/web/global-brand-database)

**Source:** [WIPO — Global Brand Database FAQ](https://www.wipo.int/en/web/global-brand-database/faqs_branddb)

**Source:** [NutriLoop — collision screening](https://nutriloop.co/)

**Source:** [Nutrivia — collision screening](https://www.nutrivia.it/)

**Source:** [Nourivo — direct nutrition-app collision](https://apps.apple.com/us/app/nourivo/id6752996030)
