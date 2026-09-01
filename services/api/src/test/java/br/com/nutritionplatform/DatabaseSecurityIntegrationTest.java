package br.com.nutritionplatform;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

class DatabaseSecurityIntegrationTest extends PostgresIntegrationTest {
    private static final List<String> DOMAIN_TABLES = List.of(
            "app_user",
            "audit_event",
            "care_relationship",
            "clinical_note_version",
            "consent_record",
            "consultation",
            "early_access_lead",
            "membership",
            "organization",
            "outbox_event",
            "patient_intake_record",
            "patient_invitation",
            "patient_person");

    @Autowired JdbcClient jdbc;

    @Test
    void enablesRowLevelSecurityForEveryExposedTable() {
        List<String> protectedTables = jdbc.sql("""
                SELECT relname
                FROM pg_class
                JOIN pg_namespace ON pg_namespace.oid = pg_class.relnamespace
                WHERE pg_namespace.nspname = 'public'
                  AND pg_class.relkind = 'r'
                  AND pg_class.relrowsecurity
                ORDER BY relname
                """).query(String.class).list();

        assertThat(protectedTables).containsExactlyElementsOf(DOMAIN_TABLES);
    }

    @Test
    void securesTheAuditTriggerFunctionSearchPath() {
        String functionConfig = jdbc.sql("""
                SELECT array_to_string(proconfig, ',')
                FROM pg_proc
                JOIN pg_namespace ON pg_namespace.oid = pg_proc.pronamespace
                WHERE pg_namespace.nspname = 'public'
                  AND pg_proc.proname = 'prevent_audit_mutation'
                """).query(String.class).single();

        assertThat(functionConfig).isEqualTo("search_path=pg_catalog");
    }

    @Test
    void securesTheImmutableRecordTriggerFunctionSearchPath() {
        String functionConfig = jdbc.sql("""
                SELECT array_to_string(proconfig, ',')
                FROM pg_proc
                JOIN pg_namespace ON pg_namespace.oid = pg_proc.pronamespace
                WHERE pg_namespace.nspname = 'public'
                  AND pg_proc.proname = 'prevent_immutable_record_mutation'
                """).query(String.class).single();

        assertThat(functionConfig).isEqualTo("search_path=pg_catalog");
    }

    @Test
    void securesTheFinalizedClinicalNoteTriggerFunctionSearchPath() {
        String functionConfig = jdbc.sql("""
                SELECT array_to_string(proconfig, ',')
                FROM pg_proc
                JOIN pg_namespace ON pg_namespace.oid = pg_proc.pronamespace
                WHERE pg_namespace.nspname = 'public'
                  AND pg_proc.proname = 'prevent_finalized_clinical_note_mutation'
                """).query(String.class).single();

        assertThat(functionConfig).isEqualTo("search_path=pg_catalog");
    }

    @Test
    void indexesEveryForeignKeyUsedByTheFoundation() {
        List<String> indexes = jdbc.sql("""
                SELECT indexname
                FROM pg_indexes
                WHERE schemaname = 'public'
                """).query(String.class).list();

        assertThat(indexes)
                .contains(
                        "ix_care_relationship_patient_person",
                        "ix_outbox_event_organization",
                        "ix_patient_invitation_patient",
                        "ix_patient_invitation_invited_by",
                        "ix_patient_invitation_accepted_by",
                        "ix_consent_record_patient",
                        "ix_consent_record_user",
                        "ix_patient_intake_updated_by",
                        "ix_consultation_relationship_time",
                        "ix_consultation_created_by",
                        "ix_clinical_note_consultation_version",
                        "ix_clinical_note_author",
                        "ix_clinical_note_amended_version");
    }
}
