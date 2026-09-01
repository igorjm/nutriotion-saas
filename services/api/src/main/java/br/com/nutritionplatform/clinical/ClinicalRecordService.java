package br.com.nutritionplatform.clinical;

import br.com.nutritionplatform.audit.AuditService;
import br.com.nutritionplatform.identity.IdentityContextService;
import br.com.nutritionplatform.identity.SessionContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClinicalRecordService {
    private final JdbcClient jdbc;
    private final IdentityContextService identityContextService;
    private final AuditService auditService;

    public ClinicalRecordService(
            JdbcClient jdbc,
            IdentityContextService identityContextService,
            AuditService auditService) {
        this.jdbc = jdbc;
        this.identityContextService = identityContextService;
        this.auditService = auditService;
    }

    @Transactional
    public PatientClinicalRecord getRecord(String externalSubject, UUID patientId) {
        SessionContext context = identityContextService.resolve(externalSubject);
        PatientContext patient = resolvePatient(context.organizationId(), patientId);
        PatientClinicalRecord record = buildRecord(patient);
        auditService.record(
                context.organizationId(),
                externalSubject,
                "PATIENT_CLINICAL_RECORD_READ",
                "PATIENT_PERSON",
                patientId);
        return record;
    }

    @Transactional
    public PatientIntakeRecord updateIntake(
            String externalSubject,
            UUID patientId,
            UpdatePatientIntakeRequest request) {
        SessionContext context = identityContextService.resolve(externalSubject);
        PatientContext patient = resolvePatient(context.organizationId(), patientId);
        PatientIntakeRecord intake = jdbc.sql("""
                INSERT INTO patient_intake_record (
                    id,
                    organization_id,
                    care_relationship_id,
                    updated_by_user_id,
                    allergies,
                    food_restrictions,
                    clinical_history,
                    routine_notes,
                    care_goal
                ) VALUES (
                    :id,
                    :organizationId,
                    :relationshipId,
                    :userId,
                    :allergies,
                    :foodRestrictions,
                    :clinicalHistory,
                    :routineNotes,
                    :careGoal
                )
                ON CONFLICT (organization_id, care_relationship_id)
                DO UPDATE SET
                    updated_by_user_id = EXCLUDED.updated_by_user_id,
                    allergies = EXCLUDED.allergies,
                    food_restrictions = EXCLUDED.food_restrictions,
                    clinical_history = EXCLUDED.clinical_history,
                    routine_notes = EXCLUDED.routine_notes,
                    care_goal = EXCLUDED.care_goal,
                    version = patient_intake_record.version + 1,
                    updated_at = CURRENT_TIMESTAMP
                RETURNING
                    allergies,
                    food_restrictions,
                    clinical_history,
                    routine_notes,
                    care_goal,
                    version,
                    updated_at
                """)
                .param("id", UUID.randomUUID())
                .param("organizationId", context.organizationId())
                .param("relationshipId", patient.relationshipId())
                .param("userId", context.userId())
                .param("allergies", normalize(request.allergies()))
                .param("foodRestrictions", normalize(request.foodRestrictions()))
                .param("clinicalHistory", normalize(request.clinicalHistory()))
                .param("routineNotes", normalize(request.routineNotes()))
                .param("careGoal", normalize(request.careGoal()))
                .query(this::mapIntake)
                .single();

        auditService.record(
                context.organizationId(),
                externalSubject,
                "PATIENT_INTAKE_UPDATED",
                "PATIENT_PERSON",
                patientId);
        return intake;
    }

    @Transactional
    public ConsultationWorkspace startConsultation(String externalSubject, UUID patientId) {
        SessionContext context = identityContextService.resolve(externalSubject);
        PatientContext patient = resolvePatient(context.organizationId(), patientId);
        ConsultationWorkspace existing = findInProgressConsultation(
                context.organizationId(), patient.relationshipId());
        if (existing != null) return existing;

        UUID consultationId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();
        jdbc.sql("""
                INSERT INTO consultation (
                    id, organization_id, care_relationship_id, created_by_user_id, status
                ) VALUES (
                    :id, :organizationId, :relationshipId, :userId, 'IN_PROGRESS'
                )
                """)
                .param("id", consultationId)
                .param("organizationId", context.organizationId())
                .param("relationshipId", patient.relationshipId())
                .param("userId", context.userId())
                .update();
        jdbc.sql("""
                INSERT INTO clinical_note_version (
                    id, organization_id, consultation_id, version, status, author_user_id
                ) VALUES (
                    :id, :organizationId, :consultationId, 1, 'DRAFT', :userId
                )
                """)
                .param("id", noteId)
                .param("organizationId", context.organizationId())
                .param("consultationId", consultationId)
                .param("userId", context.userId())
                .update();
        auditService.record(
                context.organizationId(),
                externalSubject,
                "CONSULTATION_STARTED",
                "CONSULTATION",
                consultationId);
        recordOutbox(
                context.organizationId(),
                "CONSULTATION_STARTED",
                "CONSULTATION",
                consultationId,
                patientId);
        return getConsultation(context.organizationId(), consultationId);
    }

    @Transactional
    public ConsultationWorkspace saveDraft(
            String externalSubject,
            UUID patientId,
            UUID consultationId,
            UpdateClinicalNoteRequest request) {
        SessionContext context = identityContextService.resolve(externalSubject);
        PatientContext patient = resolvePatient(context.organizationId(), patientId);
        requireConsultation(
                context.organizationId(), patient.relationshipId(), consultationId, "IN_PROGRESS");
        ClinicalNoteRecord note = getLatestNote(context.organizationId(), consultationId);
        if (!"DRAFT".equals(note.status())) {
            throw new ClinicalRecordConflictException("The finalized clinical note cannot be changed.");
        }
        jdbc.sql("""
                UPDATE clinical_note_version
                SET subjective = :subjective,
                    objective = :objective,
                    assessment = :assessment,
                    agreed_actions = :agreedActions,
                    author_user_id = :userId,
                    updated_at = CURRENT_TIMESTAMP
                WHERE organization_id = :organizationId
                  AND consultation_id = :consultationId
                  AND id = :noteId
                  AND status = 'DRAFT'
                """)
                .param("subjective", normalize(request.subjective()))
                .param("objective", normalize(request.objective()))
                .param("assessment", normalize(request.assessment()))
                .param("agreedActions", normalize(request.agreedActions()))
                .param("userId", context.userId())
                .param("organizationId", context.organizationId())
                .param("consultationId", consultationId)
                .param("noteId", note.id())
                .update();
        return getConsultation(context.organizationId(), consultationId);
    }

    @Transactional
    public ConsultationWorkspace finalizeNote(
            String externalSubject,
            UUID patientId,
            UUID consultationId) {
        SessionContext context = identityContextService.resolve(externalSubject);
        PatientContext patient = resolvePatient(context.organizationId(), patientId);
        requireConsultation(
                context.organizationId(), patient.relationshipId(), consultationId, "IN_PROGRESS");
        ClinicalNoteRecord note = getLatestNote(context.organizationId(), consultationId);
        if (!"DRAFT".equals(note.status())) {
            throw new ClinicalRecordConflictException("The clinical note is already finalized.");
        }
        if (isBlank(note.subjective())
                && isBlank(note.objective())
                && isBlank(note.assessment())
                && isBlank(note.agreedActions())) {
            throw new ClinicalRecordConflictException("Add clinical content before finalizing the note.");
        }

        jdbc.sql("""
                UPDATE clinical_note_version
                SET status = 'FINALIZED',
                    finalized_at = CURRENT_TIMESTAMP,
                    updated_at = CURRENT_TIMESTAMP,
                    author_user_id = :userId
                WHERE id = :noteId
                  AND organization_id = :organizationId
                  AND status = 'DRAFT'
                """)
                .param("userId", context.userId())
                .param("noteId", note.id())
                .param("organizationId", context.organizationId())
                .update();
        jdbc.sql("""
                UPDATE consultation
                SET status = 'FINALIZED',
                    finalized_at = CURRENT_TIMESTAMP,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = :consultationId
                  AND organization_id = :organizationId
                  AND status = 'IN_PROGRESS'
                """)
                .param("consultationId", consultationId)
                .param("organizationId", context.organizationId())
                .update();
        auditService.record(
                context.organizationId(),
                externalSubject,
                "CLINICAL_NOTE_FINALIZED",
                "CLINICAL_NOTE_VERSION",
                note.id());
        recordOutbox(
                context.organizationId(),
                "CLINICAL_NOTE_FINALIZED",
                "CLINICAL_NOTE_VERSION",
                note.id(),
                patientId);
        return getConsultation(context.organizationId(), consultationId);
    }

    @Transactional
    public ConsultationWorkspace startAmendment(
            String externalSubject,
            UUID patientId,
            UUID consultationId,
            CreateAmendmentRequest request) {
        SessionContext context = identityContextService.resolve(externalSubject);
        PatientContext patient = resolvePatient(context.organizationId(), patientId);
        requireConsultation(
                context.organizationId(), patient.relationshipId(), consultationId, "FINALIZED");
        ClinicalNoteRecord previous = getLatestNote(context.organizationId(), consultationId);
        if (!"FINALIZED".equals(previous.status())) {
            throw new ClinicalRecordConflictException("Only a finalized note can be amended.");
        }

        UUID noteId = UUID.randomUUID();
        jdbc.sql("""
                INSERT INTO clinical_note_version (
                    id,
                    organization_id,
                    consultation_id,
                    version,
                    status,
                    subjective,
                    objective,
                    assessment,
                    agreed_actions,
                    author_user_id,
                    amends_note_version_id,
                    amendment_reason
                ) VALUES (
                    :id,
                    :organizationId,
                    :consultationId,
                    :version,
                    'DRAFT',
                    :subjective,
                    :objective,
                    :assessment,
                    :agreedActions,
                    :userId,
                    :previousNoteId,
                    :reason
                )
                """)
                .param("id", noteId)
                .param("organizationId", context.organizationId())
                .param("consultationId", consultationId)
                .param("version", previous.version() + 1)
                .param("subjective", previous.subjective())
                .param("objective", previous.objective())
                .param("assessment", previous.assessment())
                .param("agreedActions", previous.agreedActions())
                .param("userId", context.userId())
                .param("previousNoteId", previous.id())
                .param("reason", normalize(request.reason()))
                .update();
        jdbc.sql("""
                UPDATE consultation
                SET status = 'IN_PROGRESS',
                    finalized_at = NULL,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = :consultationId
                  AND organization_id = :organizationId
                  AND status = 'FINALIZED'
                """)
                .param("consultationId", consultationId)
                .param("organizationId", context.organizationId())
                .update();
        auditService.record(
                context.organizationId(),
                externalSubject,
                "CLINICAL_NOTE_AMENDMENT_STARTED",
                "CLINICAL_NOTE_VERSION",
                noteId);
        return getConsultation(context.organizationId(), consultationId);
    }

    private PatientClinicalRecord buildRecord(PatientContext patient) {
        PatientIntakeRecord intake = jdbc.sql("""
                SELECT
                    allergies,
                    food_restrictions,
                    clinical_history,
                    routine_notes,
                    care_goal,
                    version,
                    updated_at
                FROM patient_intake_record
                WHERE organization_id = :organizationId
                  AND care_relationship_id = :relationshipId
                """)
                .param("organizationId", patient.organizationId())
                .param("relationshipId", patient.relationshipId())
                .query(this::mapIntake)
                .optional()
                .orElse(null);
        ConsultationWorkspace consultation = jdbc.sql("""
                SELECT id
                FROM consultation
                WHERE organization_id = :organizationId
                  AND care_relationship_id = :relationshipId
                ORDER BY created_at DESC, id
                LIMIT 1
                """)
                .param("organizationId", patient.organizationId())
                .param("relationshipId", patient.relationshipId())
                .query(UUID.class)
                .optional()
                .map(id -> getConsultation(patient.organizationId(), id))
                .orElse(null);
        return new PatientClinicalRecord(
                patient.patientId(),
                patient.displayName(),
                patient.contactEmail(),
                patient.careFocus(),
                patient.relationshipStatus(),
                intake,
                consultation);
    }

    private PatientContext resolvePatient(UUID organizationId, UUID patientId) {
        return jdbc.sql("""
                SELECT
                    p.id AS patient_id,
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
                    cr.id AS relationship_id,
                    cr.organization_id,
                    cr.care_focus,
                    cr.status
                FROM care_relationship cr
                JOIN patient_person p ON p.id = cr.patient_person_id
                LEFT JOIN app_user u ON u.id = p.user_id
                WHERE cr.organization_id = :organizationId
                  AND cr.patient_person_id = :patientId
                  AND cr.status = 'ACTIVE'
                """)
                .param("organizationId", organizationId)
                .param("patientId", patientId)
                .query((rs, rowNum) -> new PatientContext(
                        rs.getObject("patient_id", UUID.class),
                        rs.getString("display_name"),
                        rs.getString("contact_email"),
                        rs.getObject("relationship_id", UUID.class),
                        rs.getObject("organization_id", UUID.class),
                        rs.getString("care_focus"),
                        rs.getString("status")))
                .optional()
                .orElseThrow(ClinicalPatientNotFoundException::new);
    }

    private ConsultationWorkspace findInProgressConsultation(UUID organizationId, UUID relationshipId) {
        return jdbc.sql("""
                SELECT id
                FROM consultation
                WHERE organization_id = :organizationId
                  AND care_relationship_id = :relationshipId
                  AND status = 'IN_PROGRESS'
                LIMIT 1
                """)
                .param("organizationId", organizationId)
                .param("relationshipId", relationshipId)
                .query(UUID.class)
                .optional()
                .map(id -> getConsultation(organizationId, id))
                .orElse(null);
    }

    private void requireConsultation(
            UUID organizationId,
            UUID relationshipId,
            UUID consultationId,
            String requiredStatus) {
        String actualStatus = jdbc.sql("""
                SELECT status
                FROM consultation
                WHERE id = :consultationId
                  AND organization_id = :organizationId
                  AND care_relationship_id = :relationshipId
                """)
                .param("consultationId", consultationId)
                .param("organizationId", organizationId)
                .param("relationshipId", relationshipId)
                .query(String.class)
                .optional()
                .orElseThrow(ConsultationNotFoundException::new);
        if (!requiredStatus.equals(actualStatus)) {
            throw new ClinicalRecordConflictException(
                    "The consultation must be " + requiredStatus + " for this operation.");
        }
    }

    private ConsultationWorkspace getConsultation(UUID organizationId, UUID consultationId) {
        ConsultationRow consultation = jdbc.sql("""
                SELECT id, status, created_at, finalized_at
                FROM consultation
                WHERE id = :consultationId
                  AND organization_id = :organizationId
                """)
                .param("consultationId", consultationId)
                .param("organizationId", organizationId)
                .query((rs, rowNum) -> new ConsultationRow(
                        rs.getObject("id", UUID.class),
                        rs.getString("status"),
                        rs.getTimestamp("created_at").toInstant(),
                        instant(rs, "finalized_at")))
                .optional()
                .orElseThrow(ConsultationNotFoundException::new);
        return new ConsultationWorkspace(
                consultation.id(),
                consultation.status(),
                consultation.createdAt(),
                consultation.finalizedAt(),
                getLatestNote(organizationId, consultationId));
    }

    private ClinicalNoteRecord getLatestNote(UUID organizationId, UUID consultationId) {
        return jdbc.sql("""
                SELECT
                    id,
                    version,
                    status,
                    subjective,
                    objective,
                    assessment,
                    agreed_actions,
                    amendment_reason,
                    updated_at,
                    finalized_at
                FROM clinical_note_version
                WHERE organization_id = :organizationId
                  AND consultation_id = :consultationId
                ORDER BY version DESC
                LIMIT 1
                """)
                .param("organizationId", organizationId)
                .param("consultationId", consultationId)
                .query((rs, rowNum) -> new ClinicalNoteRecord(
                        rs.getObject("id", UUID.class),
                        rs.getInt("version"),
                        rs.getString("status"),
                        rs.getString("subjective"),
                        rs.getString("objective"),
                        rs.getString("assessment"),
                        rs.getString("agreed_actions"),
                        rs.getString("amendment_reason"),
                        rs.getTimestamp("updated_at").toInstant(),
                        instant(rs, "finalized_at")))
                .optional()
                .orElseThrow(ConsultationNotFoundException::new);
    }

    private PatientIntakeRecord mapIntake(ResultSet rs, int rowNum) throws SQLException {
        return new PatientIntakeRecord(
                rs.getString("allergies"),
                rs.getString("food_restrictions"),
                rs.getString("clinical_history"),
                rs.getString("routine_notes"),
                rs.getString("care_goal"),
                rs.getInt("version"),
                rs.getTimestamp("updated_at").toInstant());
    }

    private void recordOutbox(
            UUID organizationId,
            String eventType,
            String aggregateType,
            UUID aggregateId,
            UUID patientId) {
        jdbc.sql("""
                INSERT INTO outbox_event (
                    id, organization_id, event_type, aggregate_type, aggregate_id, payload
                ) VALUES (
                    :id,
                    :organizationId,
                    :eventType,
                    :aggregateType,
                    :aggregateId,
                    jsonb_build_object('patientId', CAST(:patientId AS text))
                )
                """)
                .param("id", UUID.randomUUID())
                .param("organizationId", organizationId)
                .param("eventType", eventType)
                .param("aggregateType", aggregateType)
                .param("aggregateId", aggregateId)
                .param("patientId", patientId)
                .update();
    }

    private static Instant instant(ResultSet rs, String column) throws SQLException {
        java.sql.Timestamp value = rs.getTimestamp(column);
        return value == null ? null : value.toInstant();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private record PatientContext(
            UUID patientId,
            String displayName,
            String contactEmail,
            UUID relationshipId,
            UUID organizationId,
            String careFocus,
            String relationshipStatus) {
    }

    private record ConsultationRow(
            UUID id,
            String status,
            Instant createdAt,
            Instant finalizedAt) {
    }
}
