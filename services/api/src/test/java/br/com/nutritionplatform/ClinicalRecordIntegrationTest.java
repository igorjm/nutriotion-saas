package br.com.nutritionplatform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class ClinicalRecordIntegrationTest extends PostgresIntegrationTest {
    private static final UUID PROFESSIONAL = UUID.fromString("71000000-0000-4000-8000-000000000001");
    private static final UUID OTHER_PROFESSIONAL = UUID.fromString("71000000-0000-4000-8000-000000000002");
    private static final UUID ORGANIZATION = UUID.fromString("72000000-0000-4000-8000-000000000001");
    private static final UUID OTHER_ORGANIZATION = UUID.fromString("72000000-0000-4000-8000-000000000002");
    private static final UUID PATIENT = UUID.fromString("73000000-0000-4000-8000-000000000001");
    private static final UUID EMPTY_NOTE_PATIENT = UUID.fromString("73000000-0000-4000-8000-000000000002");

    @Autowired MockMvc mockMvc;
    @Autowired JdbcClient jdbc;

    @BeforeAll
    void seedClinicalOrganizations() {
        insertUser(PROFESSIONAL, "clinical-professional", "clinical@example.invalid", "Dra. Marina");
        insertUser(OTHER_PROFESSIONAL, "clinical-other", "clinical.other@example.invalid", "Dra. Outra");
        insertOrganization(ORGANIZATION, "Clínica Marina", "clinical-org");
        insertOrganization(OTHER_ORGANIZATION, "Clínica Outra", "clinical-other-org");
        insertMembership(ORGANIZATION, PROFESSIONAL);
        insertMembership(OTHER_ORGANIZATION, OTHER_PROFESSIONAL);
        insertPatient(PATIENT, "Paciente Clínica", ORGANIZATION, "Saúde intestinal");
        insertPatient(EMPTY_NOTE_PATIENT, "Paciente Nota Vazia", ORGANIZATION, "Educação alimentar");
    }

    @Test
    void persistsIntakeAutosaveFinalizationAndAmendmentWithoutMutatingHistory() throws Exception {
        mockMvc.perform(put("/api/v1/patients/{patientId}/intake", PATIENT)
                        .header("X-Dev-Subject", "clinical-professional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "allergies": "Amendoim",
                                  "foodRestrictions": "Sem lactose",
                                  "clinicalHistory": "Histórico de desconforto intestinal",
                                  "routineNotes": "Treina no início da manhã",
                                  "careGoal": "Melhorar sintomas e energia"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version", is(1)))
                .andExpect(jsonPath("$.allergies", is("Amendoim")));

        String consultationJson = mockMvc.perform(post("/api/v1/patients/{patientId}/consultations", PATIENT)
                        .header("X-Dev-Subject", "clinical-professional"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")))
                .andExpect(jsonPath("$.note.status", is("DRAFT")))
                .andReturn()
                .getResponse()
                .getContentAsString();
        UUID consultationId = UUID.fromString(jsonField(consultationJson, "id"));

        mockMvc.perform(put("/api/v1/patients/{patientId}/consultations/{consultationId}/note", PATIENT, consultationId)
                        .header("X-Dev-Subject", "clinical-professional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "subjective": "Relata desconforto após o almoço",
                                  "objective": "Rotina alimentar irregular",
                                  "assessment": "Sintomas associados à organização da rotina",
                                  "agreedActions": "Registrar refeições por sete dias"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note.assessment", is("Sintomas associados à organização da rotina")));

        mockMvc.perform(get("/api/v1/patients/{patientId}/clinical-record", PATIENT)
                        .header("X-Dev-Subject", "clinical-professional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intake.careGoal", is("Melhorar sintomas e energia")))
                .andExpect(jsonPath("$.consultation.id", is(consultationId.toString())))
                .andExpect(jsonPath("$.consultation.note.subjective", is("Relata desconforto após o almoço")));

        String finalizedJson = mockMvc.perform(post(
                                "/api/v1/patients/{patientId}/consultations/{consultationId}/finalize",
                                PATIENT,
                                consultationId)
                        .header("X-Dev-Subject", "clinical-professional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("FINALIZED")))
                .andExpect(jsonPath("$.note.status", is("FINALIZED")))
                .andReturn()
                .getResponse()
                .getContentAsString();
        UUID firstNoteId = UUID.fromString(jsonNoteId(finalizedJson));

        mockMvc.perform(put("/api/v1/patients/{patientId}/consultations/{consultationId}/note", PATIENT, consultationId)
                        .header("X-Dev-Subject", "clinical-professional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "subjective": "",
                                  "objective": "",
                                  "assessment": "Tentativa de sobrescrever",
                                  "agreedActions": ""
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title", is("Clinical record conflict")));

        assertThatThrownBy(() -> jdbc.sql("""
                        UPDATE clinical_note_version
                        SET assessment = 'Tentativa direta'
                        WHERE id = :noteId
                        """)
                .param("noteId", firstNoteId)
                .update()).isInstanceOf(DataAccessException.class);

        mockMvc.perform(post(
                                "/api/v1/patients/{patientId}/consultations/{consultationId}/amendments",
                                PATIENT,
                                consultationId)
                        .header("X-Dev-Subject", "clinical-professional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reason\":\"Correção solicitada após revisão da consulta\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")))
                .andExpect(jsonPath("$.note.version", is(2)))
                .andExpect(jsonPath("$.note.status", is("DRAFT")));

        mockMvc.perform(put("/api/v1/patients/{patientId}/consultations/{consultationId}/note", PATIENT, consultationId)
                        .header("X-Dev-Subject", "clinical-professional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "subjective": "Relata desconforto após o jantar",
                                  "objective": "Rotina alimentar irregular",
                                  "assessment": "Sintomas associados ao horário do jantar",
                                  "agreedActions": "Registrar jantar por sete dias"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(post(
                                "/api/v1/patients/{patientId}/consultations/{consultationId}/finalize",
                                PATIENT,
                                consultationId)
                        .header("X-Dev-Subject", "clinical-professional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note.version", is(2)))
                .andExpect(jsonPath("$.note.status", is("FINALIZED")));

        assertThat(jdbc.sql("""
                        SELECT assessment
                        FROM clinical_note_version
                        WHERE id = :noteId
                        """)
                .param("noteId", firstNoteId)
                .query(String.class)
                .single()).isEqualTo("Sintomas associados à organização da rotina");
        assertThat(jdbc.sql("""
                        SELECT count(*)
                        FROM clinical_note_version
                        WHERE consultation_id = :consultationId
                        """)
                .param("consultationId", consultationId)
                .query(Integer.class)
                .single()).isEqualTo(2);
    }

    @Test
    void hidesClinicalRecordFromAnotherOrganization() throws Exception {
        mockMvc.perform(get("/api/v1/patients/{patientId}/clinical-record", PATIENT)
                        .header("X-Dev-Subject", "clinical-other"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", is("Patient not found")));
    }

    @Test
    void rejectsFinalizingAnEmptyClinicalNote() throws Exception {
        String consultationJson = mockMvc.perform(post(
                                "/api/v1/patients/{patientId}/consultations",
                                EMPTY_NOTE_PATIENT)
                        .header("X-Dev-Subject", "clinical-professional"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UUID consultationId = UUID.fromString(jsonField(consultationJson, "id"));

        mockMvc.perform(post(
                                "/api/v1/patients/{patientId}/consultations/{consultationId}/finalize",
                                EMPTY_NOTE_PATIENT,
                                consultationId)
                        .header("X-Dev-Subject", "clinical-professional"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title", is("Clinical record conflict")));
    }

    private void insertUser(UUID id, String subject, String email, String name) {
        jdbc.sql("INSERT INTO app_user (id, external_subject, email, display_name) VALUES (:id, :subject, :email, :name)")
                .param("id", id).param("subject", subject).param("email", email).param("name", name).update();
    }

    private void insertOrganization(UUID id, String name, String slug) {
        jdbc.sql("INSERT INTO organization (id, name, slug) VALUES (:id, :name, :slug)")
                .param("id", id).param("name", name).param("slug", slug).update();
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

    private void insertPatient(UUID patientId, String name, UUID organizationId, String careFocus) {
        jdbc.sql("INSERT INTO patient_person (id, display_name) VALUES (:id, :name)")
                .param("id", patientId).param("name", name).update();
        jdbc.sql("""
                INSERT INTO care_relationship (
                    id, organization_id, patient_person_id, status, care_focus
                ) VALUES (
                    :id, :organizationId, :patientId, 'ACTIVE', :careFocus
                )
                """)
                .param("id", UUID.randomUUID())
                .param("organizationId", organizationId)
                .param("patientId", patientId)
                .param("careFocus", careFocus)
                .update();
    }

    private static String jsonField(String json, String field) {
        Matcher matcher = Pattern.compile("\\\"" + field + "\\\":\\\"([^\\\"]+)\\\"").matcher(json);
        if (!matcher.find()) throw new AssertionError("Missing JSON field: " + field);
        return matcher.group(1);
    }

    private static String jsonNoteId(String json) {
        Matcher matcher = Pattern.compile("\\\"note\\\":\\{\\\"id\\\":\\\"([^\\\"]+)\\\"").matcher(json);
        if (!matcher.find()) throw new AssertionError("Missing clinical note id");
        return matcher.group(1);
    }
}
