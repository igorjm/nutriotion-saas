package br.com.nutritionplatform;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class TenantIsolationIntegrationTest extends PostgresIntegrationTest {
    private static final UUID USER_A = UUID.fromString("11000000-0000-4000-8000-000000000001");
    private static final UUID USER_B = UUID.fromString("11000000-0000-4000-8000-000000000002");
    private static final UUID USER_WITHOUT_MEMBERSHIP = UUID.fromString("11000000-0000-4000-8000-000000000003");
    private static final UUID USER_WITH_REVOKED_MEMBERSHIP = UUID.fromString("11000000-0000-4000-8000-000000000004");
    private static final UUID ORG_A = UUID.fromString("22000000-0000-4000-8000-000000000001");
    private static final UUID ORG_B = UUID.fromString("22000000-0000-4000-8000-000000000002");
    private static final UUID PATIENT_A = UUID.fromString("44000000-0000-4000-8000-000000000001");
    private static final UUID PATIENT_B = UUID.fromString("44000000-0000-4000-8000-000000000002");

    @Autowired MockMvc mockMvc;
    @Autowired JdbcClient jdbc;

    @BeforeAll
    void seedTwoOrganizations() {
        insertUser(USER_A, "subject-a", "a@example.invalid", "Nutricionista A");
        insertUser(USER_B, "subject-b", "b@example.invalid", "Nutricionista B");
        insertUser(USER_WITHOUT_MEMBERSHIP, "subject-without-membership", "missing@example.invalid", "Sem vínculo");
        insertUser(USER_WITH_REVOKED_MEMBERSHIP, "subject-revoked", "revoked@example.invalid", "Vínculo revogado");
        insertOrganization(ORG_A, "Organização A", "org-a");
        insertOrganization(ORG_B, "Organização B", "org-b");
        insertMembership(UUID.randomUUID(), ORG_A, USER_A);
        insertMembership(UUID.randomUUID(), ORG_B, USER_B);
        insertRevokedMembership(UUID.randomUUID(), ORG_A, USER_WITH_REVOKED_MEMBERSHIP);
        insertPatient(PATIENT_A, "Paciente A", ORG_A);
        insertPatient(PATIENT_B, "Paciente B", ORG_B);
    }

    @Test
    void resolvesOrganizationFromAuthenticatedMembership() throws Exception {
        mockMvc.perform(get("/api/v1/me/context").header("X-Dev-Subject", "subject-a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationId", is(ORG_A.toString())))
                .andExpect(jsonPath("$.organizationName", is("Organização A")))
                .andExpect(jsonPath("$.role", is("OWNER")));
    }

    @Test
    void allowsPatientInsideResolvedOrganizationAndAuditsRead() throws Exception {
        mockMvc.perform(get("/api/v1/patients/{patientId}", PATIENT_A)
                        .header("X-Dev-Subject", "subject-a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(PATIENT_A.toString())))
                .andExpect(jsonPath("$.displayName", is("Paciente A")));

        Integer auditCount = jdbc.sql("""
                SELECT count(*) FROM audit_event
                WHERE organization_id = :organizationId
                  AND aggregate_id = :patientId
                  AND action = 'PATIENT_RECORD_READ'
                """)
                .param("organizationId", ORG_A)
                .param("patientId", PATIENT_A)
                .query(Integer.class)
                .single();
        org.assertj.core.api.Assertions.assertThat(auditCount).isGreaterThanOrEqualTo(1);
    }

    @Test
    void returnsNotFoundForPatientOwnedByAnotherOrganization() throws Exception {
        mockMvc.perform(get("/api/v1/patients/{patientId}", PATIENT_B)
                        .header("X-Dev-Subject", "subject-a"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", is("Patient not found")));
    }

    @Test
    void deniesIdentityWithoutOrganizationMembership() throws Exception {
        mockMvc.perform(get("/api/v1/me/context")
                        .header("X-Dev-Subject", "subject-without-membership"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.title", is("Organization membership required")));
    }

    @Test
    void deniesIdentityWithRevokedOrganizationMembership() throws Exception {
        mockMvc.perform(get("/api/v1/me/context")
                        .header("X-Dev-Subject", "subject-revoked"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.title", is("Organization membership required")));
    }

    private void insertUser(UUID id, String subject, String email, String name) {
        jdbc.sql("INSERT INTO app_user (id, external_subject, email, display_name) VALUES (:id, :subject, :email, :name)")
                .param("id", id).param("subject", subject).param("email", email).param("name", name).update();
    }

    private void insertOrganization(UUID id, String name, String slug) {
        jdbc.sql("INSERT INTO organization (id, name, slug) VALUES (:id, :name, :slug)")
                .param("id", id).param("name", name).param("slug", slug).update();
    }

    private void insertMembership(UUID id, UUID organizationId, UUID userId) {
        jdbc.sql("""
                INSERT INTO membership (id, organization_id, user_id, role, status, is_default)
                VALUES (:id, :organizationId, :userId, 'OWNER', 'ACTIVE', TRUE)
                """)
                .param("id", id).param("organizationId", organizationId).param("userId", userId).update();
    }

    private void insertRevokedMembership(UUID id, UUID organizationId, UUID userId) {
        jdbc.sql("""
                INSERT INTO membership (id, organization_id, user_id, role, status, is_default)
                VALUES (:id, :organizationId, :userId, 'OWNER', 'REVOKED', TRUE)
                """)
                .param("id", id).param("organizationId", organizationId).param("userId", userId).update();
    }

    private void insertPatient(UUID id, String name, UUID organizationId) {
        jdbc.sql("INSERT INTO patient_person (id, display_name) VALUES (:id, :name)")
                .param("id", id).param("name", name).update();
        jdbc.sql("""
                INSERT INTO care_relationship (id, organization_id, patient_person_id, status)
                VALUES (:id, :organizationId, :patientId, 'ACTIVE')
                """)
                .param("id", UUID.randomUUID())
                .param("organizationId", organizationId)
                .param("patientId", id)
                .update();
    }
}
