package br.com.nutritionplatform.platform.config;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
@Profile("local-auth")
class LocalAuthDataInitializer {
    private static final UUID USER_ID = UUID.fromString("10000000-0000-4000-8000-000000000001");
    private static final UUID ORGANIZATION_ID = UUID.fromString("20000000-0000-4000-8000-000000000001");
    private static final UUID MEMBERSHIP_ID = UUID.fromString("30000000-0000-4000-8000-000000000001");
    private static final UUID PATIENT_ID = UUID.fromString("40000000-0000-4000-8000-000000000001");
    private static final UUID RELATIONSHIP_ID = UUID.fromString("50000000-0000-4000-8000-000000000001");

    @Bean
    CommandLineRunner insertLocalAuthFictionalData(
            JdbcClient jdbc,
            @Value("${app.local-auth.seed-email}") String seedEmail) {
        return args -> {
            UUID externalSubject = jdbc.sql("""
                    SELECT id
                    FROM auth.users
                    WHERE lower(email) = lower(:email)
                      AND deleted_at IS NULL
                    LIMIT 1
                    """)
                    .param("email", seedEmail)
                    .query(UUID.class)
                    .optional()
                    .orElseThrow(() -> new IllegalStateException(
                            "Local Auth fixture is missing. Run scripts/bootstrap-local-auth.sh first."));

            jdbc.sql("""
                    INSERT INTO app_user (id, external_subject, email, display_name)
                    VALUES (:id, :externalSubject, :email, 'Mariana Costa')
                    ON CONFLICT (id) DO UPDATE SET
                        external_subject = EXCLUDED.external_subject,
                        email = EXCLUDED.email,
                        display_name = EXCLUDED.display_name
                    """)
                    .param("id", USER_ID)
                    .param("externalSubject", externalSubject.toString())
                    .param("email", seedEmail)
                    .update();
            jdbc.sql("""
                    INSERT INTO organization (id, name, slug)
                    VALUES (:id, 'Consultório Mariana Costa', 'consultorio-mariana-local')
                    ON CONFLICT (id) DO UPDATE SET
                        name = EXCLUDED.name,
                        slug = EXCLUDED.slug
                    """).param("id", ORGANIZATION_ID).update();
            jdbc.sql("""
                    INSERT INTO membership (id, organization_id, user_id, role, status, is_default)
                    VALUES (:id, :organizationId, :userId, 'OWNER', 'ACTIVE', TRUE)
                    ON CONFLICT (id) DO UPDATE SET
                        organization_id = EXCLUDED.organization_id,
                        user_id = EXCLUDED.user_id,
                        role = EXCLUDED.role,
                        status = EXCLUDED.status,
                        is_default = EXCLUDED.is_default
                    """)
                    .param("id", MEMBERSHIP_ID)
                    .param("organizationId", ORGANIZATION_ID)
                    .param("userId", USER_ID)
                    .update();
            jdbc.sql("""
                    INSERT INTO patient_person (id, display_name)
                    VALUES (:id, 'Camila Ribeiro')
                    ON CONFLICT (id) DO UPDATE SET display_name = EXCLUDED.display_name
                    """).param("id", PATIENT_ID).update();
            jdbc.sql("""
                    INSERT INTO care_relationship (id, organization_id, patient_person_id, status)
                    VALUES (:id, :organizationId, :patientId, 'ACTIVE')
                    ON CONFLICT (id) DO UPDATE SET
                        organization_id = EXCLUDED.organization_id,
                        patient_person_id = EXCLUDED.patient_person_id,
                        status = EXCLUDED.status
                    """)
                    .param("id", RELATIONSHIP_ID)
                    .param("organizationId", ORGANIZATION_ID)
                    .param("patientId", PATIENT_ID)
                    .update();
        };
    }
}
