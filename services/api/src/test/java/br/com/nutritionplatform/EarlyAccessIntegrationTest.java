package br.com.nutritionplatform;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class EarlyAccessIntegrationTest extends PostgresIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired JdbcClient jdbc;

    @Test
    void recordsConsentAndEmitsNonSensitiveOutboxReference() throws Exception {
        mockMvc.perform(post("/api/v1/public/early-access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Nutricionista Piloto",
                                  "email": "piloto@example.invalid",
                                  "currentTool": "WebDiet",
                                  "source": "integration-test",
                                  "marketingConsent": false,
                                  "consentTextVersion": "early-access-v1"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("REGISTERED")));

        Integer leadCount = jdbc.sql("""
                SELECT count(*) FROM early_access_lead
                WHERE email = 'piloto@example.invalid'
                  AND marketing_consent = FALSE
                  AND consent_text_version = 'early-access-v1'
                """).query(Integer.class).single();
        org.assertj.core.api.Assertions.assertThat(leadCount).isEqualTo(1);

        String payload = jdbc.sql("""
                SELECT payload::text FROM outbox_event
                WHERE event_type = 'EarlyAccessLeadRegistered'
                ORDER BY occurred_at DESC LIMIT 1
                """).query(String.class).single();
        org.assertj.core.api.Assertions.assertThat(payload)
                .contains("leadId")
                .doesNotContain("piloto@example.invalid")
                .doesNotContain("Nutricionista Piloto");
    }

    @Test
    void rejectsInvalidLeadWithoutPersistingIt() throws Exception {
        mockMvc.perform(post("/api/v1/public/early-access")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "X",
                                  "email": "not-an-email",
                                  "currentTool": "",
                                  "source": "test",
                                  "marketingConsent": false,
                                  "consentTextVersion": "INVALID VERSION"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Request validation failed")));
    }
}
