package br.com.nutritionplatform.patient;

import br.com.nutritionplatform.audit.AuditService;
import br.com.nutritionplatform.identity.IdentityContextService;
import br.com.nutritionplatform.identity.SessionContext;
import java.util.UUID;
import java.util.List;
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

    @Transactional(readOnly = true)
    public List<PatientListItem> listForAuthenticatedSubject(String externalSubject) {
        SessionContext context = identityContextService.resolve(externalSubject);
        return jdbc.sql("""
                SELECT
                    p.id,
                    p.display_name,
                    COALESCE(
                        u.email,
                        (
                            SELECT pi.email
                            FROM patient_invitation pi
                            WHERE pi.patient_person_id = p.id
                            ORDER BY pi.created_at DESC
                            LIMIT 1
                        )
                    ) AS contact_email,
                    cr.care_focus,
                    cr.status,
                    cr.created_at
                FROM care_relationship cr
                JOIN patient_person p ON p.id = cr.patient_person_id
                LEFT JOIN app_user u ON u.id = p.user_id
                WHERE cr.organization_id = :organizationId
                  AND cr.status <> 'ARCHIVED'
                ORDER BY
                    CASE cr.status WHEN 'INVITED' THEN 0 ELSE 1 END,
                    lower(p.display_name),
                    p.id
                """)
                .param("organizationId", context.organizationId())
                .query((rs, rowNum) -> new PatientListItem(
                        rs.getObject("id", UUID.class),
                        rs.getString("display_name"),
                        rs.getString("contact_email"),
                        rs.getString("care_focus"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at").toInstant()))
                .list();
    }
}
