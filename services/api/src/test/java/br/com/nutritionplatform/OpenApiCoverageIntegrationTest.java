package br.com.nutritionplatform;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class OpenApiCoverageIntegrationTest extends PostgresIntegrationTest {
    @Autowired MockMvc mockMvc;

    @Test
    void runtimeDocumentationContainsVersionedWalkingSkeletonOperations() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/api/v1/public/early-access")))
                .andExpect(content().string(containsString("/api/v1/me/context")))
                .andExpect(content().string(containsString("/api/v1/patients/{patientId}")))
                .andExpect(content().string(containsString("/api/v1/patients/{patientId}/clinical-record")))
                .andExpect(content().string(containsString("/api/v1/patients/{patientId}/consultations")))
                .andExpect(content().string(containsString("/api/v1/patient-invitations")))
                .andExpect(content().string(containsString("/api/v1/public/patient-invitations/{token}")));
    }
}
