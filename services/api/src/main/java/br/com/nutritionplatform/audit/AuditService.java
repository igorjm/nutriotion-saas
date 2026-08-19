package br.com.nutritionplatform.audit;

import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final JdbcClient jdbc;

    public AuditService(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public UUID record(
            UUID organizationId,
            String actorSubject,
            String action,
            String aggregateType,
            UUID aggregateId) {
        UUID eventId = UUID.randomUUID();
        jdbc.sql("""
                INSERT INTO audit_event (
                    id, organization_id, actor_subject, action, aggregate_type, aggregate_id
                ) VALUES (
                    :id, :organizationId, :actorSubject, :action, :aggregateType, :aggregateId
                )
                """)
                .param("id", eventId)
                .param("organizationId", organizationId)
                .param("actorSubject", actorSubject)
                .param("action", action)
                .param("aggregateType", aggregateType)
                .param("aggregateId", aggregateId)
                .update();
        return eventId;
    }
}
