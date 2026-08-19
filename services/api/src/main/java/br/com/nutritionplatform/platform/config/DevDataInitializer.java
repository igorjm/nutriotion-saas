package br.com.nutritionplatform.platform.config;

import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
@Profile("dev")
class DevDataInitializer {
    static final UUID USER_ID = UUID.fromString("10000000-0000-4000-8000-000000000001");
    static final UUID ORGANIZATION_ID = UUID.fromString("20000000-0000-4000-8000-000000000001");
    static final UUID MEMBERSHIP_ID = UUID.fromString("30000000-0000-4000-8000-000000000001");
    static final UUID PATIENT_ID = UUID.fromString("40000000-0000-4000-8000-000000000001");
    static final UUID RELATIONSHIP_ID = UUID.fromString("50000000-0000-4000-8000-000000000001");

    @Bean
    CommandLineRunner insertFictionalDevelopmentData(JdbcClient jdbc) {
        return args -> {
            jdbc.sql("""
                    INSERT INTO app_user (id, external_subject, email, display_name)
                    VALUES (:id, 'dev-nutritionist', 'mariana@example.invalid', 'Mariana Costa')
                    ON CONFLICT DO NOTHING
                    """).param("id", USER_ID).update();
            jdbc.sql("""
                    INSERT INTO organization (id, name, slug)
                    VALUES (:id, 'Consultório Mariana Costa', 'consultorio-mariana-dev')
                    ON CONFLICT DO NOTHING
                    """).param("id", ORGANIZATION_ID).update();
            jdbc.sql("""
                    INSERT INTO membership (id, organization_id, user_id, role, status, is_default)
                    VALUES (:id, :organizationId, :userId, 'OWNER', 'ACTIVE', TRUE)
                    ON CONFLICT DO NOTHING
                    """)
                    .param("id", MEMBERSHIP_ID)
                    .param("organizationId", ORGANIZATION_ID)
                    .param("userId", USER_ID)
                    .update();
            jdbc.sql("""
                    INSERT INTO patient_person (id, display_name)
                    VALUES (:id, 'Camila Ribeiro')
                    ON CONFLICT DO NOTHING
                    """).param("id", PATIENT_ID).update();
            jdbc.sql("""
                    INSERT INTO care_relationship (id, organization_id, patient_person_id, status)
                    VALUES (:id, :organizationId, :patientId, 'ACTIVE')
                    ON CONFLICT DO NOTHING
                    """)
                    .param("id", RELATIONSHIP_ID)
                    .param("organizationId", ORGANIZATION_ID)
                    .param("patientId", PATIENT_ID)
                    .update();
        };
    }
}
