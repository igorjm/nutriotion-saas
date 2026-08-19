package br.com.nutritionplatform.patient;

import br.com.nutritionplatform.audit.AuditService;
import br.com.nutritionplatform.identity.IdentityContextService;
import br.com.nutritionplatform.identity.SessionContext;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientQueryService {
    private final JdbcClient jdbc;
    private final IdentityContextService identityContextService;
    private final AuditService auditService;

    public PatientQueryService(
            JdbcClient jdbc,
            IdentityContextService identityContextService,
            AuditService auditService) {
        this.jdbc = jdbc;
        this.identityContextService = identityContextService;
        this.auditService = auditService;
    }

    @Transactional
    public PatientSummary getForAuthenticatedSubject(String externalSubject, UUID patientId) {
        SessionContext context = identityContextService.resolve(externalSubject);
        PatientSummary patient = jdbc.sql("""
                SELECT p.id, p.display_name, cr.status
                FROM patient_person p
                JOIN care_relationship cr ON cr.patient_person_id = p.id
                WHERE p.id = :patientId
                  AND cr.organization_id = :organizationId
                  AND cr.status <> 'ARCHIVED'
                """)
                .param("patientId", patientId)
                .param("organizationId", context.organizationId())
                .query((rs, rowNum) -> new PatientSummary(
                        rs.getObject("id", UUID.class),
                        rs.getString("display_name"),
                        rs.getString("status")))
                .optional()
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        auditService.record(
                context.organizationId(),
                externalSubject,
                "PATIENT_RECORD_READ",
                "PATIENT_PERSON",
                patientId);
        return patient;
    }
}
