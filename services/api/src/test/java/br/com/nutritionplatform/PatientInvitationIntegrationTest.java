package br.com.nutritionplatform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class PatientInvitationIntegrationTest extends PostgresIntegrationTest {
    private static final UUID PROFESSIONAL_USER = UUID.fromString("61000000-0000-4000-8000-000000000001");
    private static final UUID OTHER_PROFESSIONAL_USER = UUID.fromString("61000000-0000-4000-8000-000000000002");
    private static final UUID ORGANIZATION = UUID.fromString("62000000-0000-4000-8000-000000000001");
    private static final UUID OTHER_ORGANIZATION = UUID.fromString("62000000-0000-4000-8000-000000000002");

    @Autowired MockMvc mockMvc;
    @Autowired JdbcClient jdbc;

    @BeforeAll
    void seedProfessionals() {
        insertUser(PROFESSIONAL_USER, "invitation-professional", "professional@example.invalid", "Mariana Costa");
        insertUser(OTHER_PROFESSIONAL_USER, "other-professional", "other@example.invalid", "Outra Nutricionista");
        insertOrganization(ORGANIZATION, "Consultório Mariana", "invitation-org");
        insertOrganization(OTHER_ORGANIZATION, "Outra Organização", "other-invitation-org");
        insertMembership(ORGANIZATION, PROFESSIONAL_USER);
        insertMembership(OTHER_ORGANIZATION, OTHER_PROFESSIONAL_USER);
    }

    @Test
    void completesInvitationConsentAndRelationshipActivationAtomically() throws Exception {
        CreatedInvitation created = createInvitation(
                "Camila Ribeiro",
                "camila.invited@example.invalid",
                "Performance e rotina");

        mockMvc.perform(get("/api/v1/public/patient-invitations/{token}", created.token()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationName", is("Consultório Mariana")))
                .andExpect(jsonPath("$.patientDisplayName", is("Camila Ribeiro")))
                .andExpect(jsonPath("$.maskedEmail", is("c***@example.invalid")))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.consentTextVersion", is("care-relationship-v1")))
                .andExpect(jsonPath("$.consentText", is("""
                        Ao aceitar, você permite que esta organização crie e mantenha seu registro de acompanhamento nutricional, acesse as informações que você decidir fornecer e entre em contato sobre este cuidado.

                        Este consentimento não inclui marketing, não autoriza decisões clínicas automáticas e poderá ser revisto ou retirado pelos canais de privacidade quando esse fluxo estiver disponível.""")));

        mockMvc.perform(get("/api/v1/patients")
                        .header("X-Dev-Subject", "invitation-professional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItem(created.patientId().toString())))
                .andExpect(jsonPath("$[?(@.id == '%s')].relationshipStatus"
                        .formatted(created.patientId()), hasItem("INVITED")));

        mockMvc.perform(post("/api/v1/patient-invitations/{token}/accept", created.token())
                        .with(jwt().jwt(token -> token
                                .subject("invited-patient-subject")
                                .claim("email", "camila.invited@example.invalid")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"consentTextVersion\":\"care-relationship-v1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId", is(created.patientId().toString())))
                .andExpect(jsonPath("$.relationshipStatus", is("ACTIVE")));

        assertThat(count("consent_record", "patient_person_id", created.patientId())).isEqualTo(1);
        String consentSnapshot = jdbc.sql("""
                SELECT text_snapshot
                FROM consent_record
                WHERE patient_person_id = :patientId
                """)
                .param("patientId", created.patientId())
                .query(String.class)
                .single();
        assertThat(consentSnapshot).contains("não autoriza decisões clínicas automáticas");
        assertThat(count("audit_event", "aggregate_id", created.patientId())).isGreaterThanOrEqualTo(2);
        assertThat(count("outbox_event", "aggregate_id", created.patientId())).isGreaterThanOrEqualTo(2);

        String relationshipStatus = jdbc.sql("""
                SELECT status FROM care_relationship
                WHERE organization_id = :organizationId
                  AND patient_person_id = :patientId
                """)
                .param("organizationId", ORGANIZATION)
                .param("patientId", created.patientId())
                .query(String.class)
                .single();
        assertThat(relationshipStatus).isEqualTo("ACTIVE");

        mockMvc.perform(post("/api/v1/patient-invitations/{token}/accept", created.token())
                        .with(jwt().jwt(token -> token
                                .subject("invited-patient-subject")
                                .claim("email", "camila.invited@example.invalid")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"consentTextVersion\":\"care-relationship-v1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relationshipStatus", is("ACTIVE")));
        assertThat(count("consent_record", "patient_person_id", created.patientId())).isEqualTo(1);
    }

    @Test
    void rejectsAValidAccountWhoseEmailDoesNotMatchTheInvitation() throws Exception {
        CreatedInvitation created = createInvitation(
                "Paciente Incorreta",
                "expected@example.invalid",
                null);

        mockMvc.perform(post("/api/v1/patient-invitations/{token}/accept", created.token())
                        .with(jwt().jwt(token -> token
                                .subject("wrong-patient-subject")
                                .claim("email", "wrong@example.invalid")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"consentTextVersion\":\"care-relationship-v1\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.title", is("Patient invitation identity mismatch")));

        assertThat(count("consent_record", "patient_person_id", created.patientId())).isZero();
    }

    @Test
    void hidesInvitedPatientsFromOtherOrganizations() throws Exception {
        CreatedInvitation created = createInvitation(
                "Paciente Isolada",
                "isolated@example.invalid",
                "Educação alimentar");

        mockMvc.perform(get("/api/v1/patients")
                        .header("X-Dev-Subject", "other-professional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", not(hasItem(created.patientId().toString()))));
    }

    @Test
    void preventsTwoLiveInvitationsForTheSameOrganizationAndEmail() throws Exception {
        createInvitation("Primeiro Convite", "duplicate@example.invalid", null);

        mockMvc.perform(post("/api/v1/patient-invitations")
                        .header("X-Dev-Subject", "invitation-professional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "displayName": "Segundo Convite",
                                  "email": "DUPLICATE@example.invalid"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title", is("Patient invitation conflict")));
    }

    @Test
    void reusesOnePatientIdentityAcrossTwoOrganizations() throws Exception {
        String email = "shared.patient@example.invalid";
        CreatedInvitation first = createInvitation(
                "Paciente Compartilhada",
                email,
                "Educação alimentar");
        acceptInvitation(first, "shared-patient-subject", email);

        CreatedInvitation second = createInvitation(
                "other-professional",
                "Paciente Compartilhada",
                email,
                "Saúde clínica");

        assertThat(second.patientId()).isEqualTo(first.patientId());
        acceptInvitation(second, "shared-patient-subject", email);
        assertThat(count("care_relationship", "patient_person_id", first.patientId())).isEqualTo(2);
        assertThat(count("patient_person", "user_id", userId("shared-patient-subject"))).isEqualTo(1);
    }

    @Test
    void rejectsAConsentVersionThatWasNotServedByTheApi() throws Exception {
        CreatedInvitation created = createInvitation(
                "Paciente com cliente antigo",
                "stale.client@example.invalid",
                null);

        mockMvc.perform(post("/api/v1/patient-invitations/{token}/accept", created.token())
                        .with(jwt().jwt(token -> token
                                .subject("stale-client-subject")
                                .claim("email", "stale.client@example.invalid")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"consentTextVersion\":\"legacy-client-v0\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title", is("Patient invitation conflict")));

        assertThat(count("consent_record", "patient_person_id", created.patientId())).isZero();
    }

    private CreatedInvitation createInvitation(String name, String email, String careFocus) throws Exception {
        return createInvitation("invitation-professional", name, email, careFocus);
    }

    private CreatedInvitation createInvitation(
            String professionalSubject,
            String name,
            String email,
            String careFocus) throws Exception {
        String careFocusJson = careFocus == null ? "null" : jsonString(careFocus);
        String response = mockMvc.perform(post("/api/v1/patient-invitations")
                        .header("X-Dev-Subject", professionalSubject)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "displayName": %s,
                                  "email": %s,
                                  "careFocus": %s
                                }
                                """.formatted(
                                jsonString(name),
                                jsonString(email),
                                careFocusJson)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return new CreatedInvitation(
                UUID.fromString(jsonField(response, "patientId")),
                jsonField(response, "token"));
    }

    private void acceptInvitation(CreatedInvitation invitation, String subject, String email) throws Exception {
        mockMvc.perform(post("/api/v1/patient-invitations/{token}/accept", invitation.token())
                        .with(jwt().jwt(token -> token.subject(subject).claim("email", email)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"consentTextVersion\":\"care-relationship-v1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relationshipStatus", is("ACTIVE")));
    }

    private UUID userId(String subject) {
        return jdbc.sql("SELECT id FROM app_user WHERE external_subject = :subject")
                .param("subject", subject)
                .query(UUID.class)
                .single();
    }

    private static String jsonString(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private static String jsonField(String json, String field) {
        Matcher matcher = Pattern.compile("\\\"" + field + "\\\":\\\"([^\\\"]+)\\\"").matcher(json);
        if (!matcher.find()) throw new AssertionError("Missing JSON field: " + field);
        return matcher.group(1);
    }

    private int count(String table, String column, UUID value) {
        return jdbc.sql("SELECT count(*) FROM " + table + " WHERE " + column + " = :value")
                .param("value", value)
                .query(Integer.class)
                .single();
    }

    private void insertUser(UUID id, String subject, String email, String name) {
        jdbc.sql("""
                INSERT INTO app_user (id, external_subject, email, display_name)
                VALUES (:id, :subject, :email, :name)
                """)
                .param("id", id)
                .param("subject", subject)
                .param("email", email)
                .param("name", name)
                .update();
    }

    private void insertOrganization(UUID id, String name, String slug) {
        jdbc.sql("INSERT INTO organization (id, name, slug) VALUES (:id, :name, :slug)")
                .param("id", id)
                .param("name", name)
                .param("slug", slug)
                .update();
    }

    private void insertMembership(UUID organizationId, UUID userId) {
        jdbc.sql("""
                INSERT INTO membership (id, organization_id, user_id, role, status, is_default)
                VALUES (:id, :organizationId, :userId, 'OWNER', 'ACTIVE', TRUE)
                """)
                .param("id", UUID.randomUUID())
                .param("organizationId", organizationId)
                .param("userId", userId)
                .update();
    }

    private record CreatedInvitation(UUID patientId, String token) {
    }
}
