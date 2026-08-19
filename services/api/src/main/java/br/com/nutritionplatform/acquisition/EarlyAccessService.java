package br.com.nutritionplatform.acquisition;

import br.com.nutritionplatform.audit.AuditService;
import java.util.Locale;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EarlyAccessService {
    private final JdbcClient jdbc;
    private final AuditService auditService;

    public EarlyAccessService(JdbcClient jdbc, AuditService auditService) {
        this.jdbc = jdbc;
        this.auditService = auditService;
    }

    @Transactional
    public EarlyAccessResponse register(EarlyAccessRequest request) {
        UUID proposedId = UUID.randomUUID();
        UUID leadId = jdbc.sql("""
                INSERT INTO early_access_lead (
                    id, name, email, current_tool, source, marketing_consent, consent_text_version
                ) VALUES (
                    :id, :name, :email, :currentTool, :source, :marketingConsent, :consentTextVersion
                )
                ON CONFLICT ((lower(email))) DO UPDATE SET
                    name = EXCLUDED.name,
                    current_tool = EXCLUDED.current_tool,
                    source = EXCLUDED.source,
                    marketing_consent = EXCLUDED.marketing_consent,
                    consent_text_version = EXCLUDED.consent_text_version,
                    received_at = CURRENT_TIMESTAMP
                RETURNING id
                """)
                .param("id", proposedId)
                .param("name", request.name().trim())
                .param("email", request.email().trim().toLowerCase(Locale.ROOT))
                .param("currentTool", request.currentTool().trim())
                .param("source", request.source().trim())
                .param("marketingConsent", request.marketingConsent())
                .param("consentTextVersion", request.consentTextVersion())
                .query(UUID.class)
                .single();

        auditService.record(null, null, "EARLY_ACCESS_REGISTERED", "EARLY_ACCESS_LEAD", leadId);
        enqueueLeadRegistered(leadId);
        return new EarlyAccessResponse(leadId, "REGISTERED");
    }

    private void enqueueLeadRegistered(UUID leadId) {
        jdbc.sql("""
                INSERT INTO outbox_event (
                    id, event_type, aggregate_type, aggregate_id, payload
                ) VALUES (
                    :id, 'EarlyAccessLeadRegistered', 'EARLY_ACCESS_LEAD', :leadId,
                    jsonb_build_object('leadId', CAST(:leadId AS text))
                )
                """)
                .param("id", UUID.randomUUID())
                .param("leadId", leadId)
                .update();
    }
}
