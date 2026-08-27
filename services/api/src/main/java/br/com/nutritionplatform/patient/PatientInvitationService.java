package br.com.nutritionplatform.patient;

import br.com.nutritionplatform.audit.AuditService;
import br.com.nutritionplatform.identity.IdentityContextService;
import br.com.nutritionplatform.identity.SessionContext;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientInvitationService {
    private static final Duration INVITATION_LIFETIME = Duration.ofDays(7);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String CONSENT_PURPOSE = "CARE_RELATIONSHIP";
    static final String CONSENT_TEXT_VERSION = "care-relationship-v1";
    static final String CONSENT_TEXT = """
            Ao aceitar, você permite que esta organização crie e mantenha seu registro de acompanhamento nutricional, acesse as informações que você decidir fornecer e entre em contato sobre este cuidado.

            Este consentimento não inclui marketing, não autoriza decisões clínicas automáticas e poderá ser revisto ou retirado pelos canais de privacidade quando esse fluxo estiver disponível.""";

    private final JdbcClient jdbc;
    private final IdentityContextService identityContextService;
    private final AuditService auditService;
    private final Clock clock;

    public PatientInvitationService(
            JdbcClient jdbc,
            IdentityContextService identityContextService,
            AuditService auditService) {
        this.jdbc = jdbc;
        this.identityContextService = identityContextService;
        this.auditService = auditService;
        this.clock = Clock.systemUTC();
    }

    @Transactional
    public PatientInvitationCreated create(
            String externalSubject,
            CreatePatientInvitationRequest request) {
        SessionContext context = identityContextService.resolve(externalSubject);
        String email = normalizeEmail(request.email());
        String displayName = request.displayName().trim();
        String careFocus = normalizeNullable(request.careFocus());

        boolean pendingExists = jdbc.sql("""
                SELECT EXISTS (
                    SELECT 1
                    FROM patient_invitation
                    WHERE organization_id = :organizationId
                      AND lower(email) = :email
                      AND status = 'PENDING'
                      AND expires_at > CURRENT_TIMESTAMP
                )
                """)
                .param("organizationId", context.organizationId())
                .param("email", email)
                .query(Boolean.class)
                .single();
        if (pendingExists) {
            throw new PatientInvitationConflictException(
                    "An active invitation already exists for this email in the organization.");
        }

        expireOlderPendingInvitation(context.organizationId(), email);

        UUID patientId = findPatientPersonId(email);
        boolean newPatient = patientId == null;
        if (newPatient) patientId = UUID.randomUUID();
        UUID relationshipId = UUID.randomUUID();
        UUID invitationId = UUID.randomUUID();
        String token = generateToken();
        Instant expiresAt = clock.instant().plus(INVITATION_LIFETIME);

        if (newPatient) {
            jdbc.sql("""
                    INSERT INTO patient_person (id, display_name)
                    VALUES (:id, :displayName)
                    """)
                    .param("id", patientId)
                    .param("displayName", displayName)
                    .update();
        }
        try {
            jdbc.sql("""
                    INSERT INTO care_relationship (
                        id, organization_id, patient_person_id, status, care_focus
                    ) VALUES (
                        :id, :organizationId, :patientId, 'INVITED', :careFocus
                    )
                    """)
                    .param("id", relationshipId)
                    .param("organizationId", context.organizationId())
                    .param("patientId", patientId)
                    .param("careFocus", careFocus)
                    .update();
        } catch (DataIntegrityViolationException exception) {
            throw new PatientInvitationConflictException(
                    "A care relationship already exists for this patient in the organization.");
        }
        try {
            jdbc.sql("""
                    INSERT INTO patient_invitation (
                        id, organization_id, patient_person_id, invited_by_user_id,
                        email, token_hash, status, expires_at
                    ) VALUES (
                        :id, :organizationId, :patientId, :invitedByUserId,
                        :email, :tokenHash, 'PENDING', :expiresAt
                    )
                    """)
                    .param("id", invitationId)
                    .param("organizationId", context.organizationId())
                    .param("patientId", patientId)
                    .param("invitedByUserId", context.userId())
                    .param("email", email)
                    .param("tokenHash", hashToken(token))
                    .param("expiresAt", OffsetDateTime.ofInstant(expiresAt, ZoneOffset.UTC))
                    .update();
        } catch (DataIntegrityViolationException exception) {
            throw new PatientInvitationConflictException(
                    "An active invitation already exists for this email in the organization.");
        }

        auditService.record(
                context.organizationId(),
                externalSubject,
                "PATIENT_INVITATION_CREATED",
                "PATIENT_PERSON",
                patientId);
        recordOutboxEvent(
                context.organizationId(),
                "PATIENT_INVITATION_CREATED",
                "PATIENT_PERSON",
                patientId,
                "{\"invitationId\":\"" + invitationId + "\"}");

        return new PatientInvitationCreated(
                invitationId,
                patientId,
                token,
                "PENDING",
                expiresAt);
    }

    public PatientInvitationPreview preview(String token) {
        InvitationRow invitation = findInvitation(token, false);
        String status = invitation.status();
        if ("PENDING".equals(status) && !invitation.expiresAt().isAfter(clock.instant())) {
            status = "EXPIRED";
        }
        return new PatientInvitationPreview(
                invitation.organizationName(),
                invitation.patientDisplayName(),
                maskEmail(invitation.email()),
                status,
                invitation.expiresAt(),
                CONSENT_TEXT_VERSION,
                CONSENT_TEXT);
    }

    @Transactional
    public PatientInvitationAcceptanceResponse accept(
            String token,
            String externalSubject,
            String authenticatedEmail,
            PatientInvitationAcceptanceRequest request) {
        InvitationRow invitation = findInvitation(token, true);
        String email = normalizeEmail(authenticatedEmail);

        if (!invitation.email().equalsIgnoreCase(email)) {
            throw new PatientInvitationIdentityMismatchException();
        }
        if (!CONSENT_TEXT_VERSION.equals(request.consentTextVersion())) {
            throw new PatientInvitationConflictException(
                    "The consent text changed. Reload the invitation before accepting it.");
        }
        if ("ACCEPTED".equals(invitation.status())) {
            UUID acceptedUserId = findUserId(externalSubject, email);
            if (!acceptedUserId.equals(invitation.acceptedByUserId())) {
                throw new PatientInvitationNotFoundException();
            }
            return new PatientInvitationAcceptanceResponse(
                    invitation.patientPersonId(),
                    "ACTIVE",
                    invitation.acceptedAt());
        }
        if (!"PENDING".equals(invitation.status())
                || !invitation.expiresAt().isAfter(clock.instant())) {
            throw new PatientInvitationNotFoundException();
        }

        UUID userId = resolveOrCreatePatientUser(
                externalSubject,
                email,
                invitation.patientDisplayName());
        Instant acceptedAt = clock.instant();

        UUID patientId = linkPatientIdentity(invitation, userId);

        int activatedRelationship = jdbc.sql("""
                UPDATE care_relationship
                SET status = 'ACTIVE'
                WHERE organization_id = :organizationId
                  AND patient_person_id = :patientId
                  AND status = 'INVITED'
                """)
                .param("organizationId", invitation.organizationId())
                .param("patientId", patientId)
                .update();
        if (activatedRelationship != 1) {
            throw new PatientInvitationConflictException(
                    "The care relationship cannot be activated from its current state.");
        }
        int acceptedInvitation = jdbc.sql("""
                UPDATE patient_invitation
                SET status = 'ACCEPTED',
                    accepted_by_user_id = :userId,
                    accepted_at = :acceptedAt
                WHERE id = :invitationId
                  AND status = 'PENDING'
                """)
                .param("userId", userId)
                .param("acceptedAt", OffsetDateTime.ofInstant(acceptedAt, ZoneOffset.UTC))
                .param("invitationId", invitation.id())
                .update();
        if (acceptedInvitation != 1) {
            throw new PatientInvitationConflictException(
                    "The invitation was changed while it was being accepted.");
        }
        jdbc.sql("""
                INSERT INTO consent_record (
                    id, organization_id, patient_person_id, user_id, invitation_id,
                    purpose, text_version, channel, accepted_at, text_snapshot
                ) VALUES (
                    :id, :organizationId, :patientId, :userId, :invitationId,
                    :purpose, :textVersion, 'WEB_INVITATION', :acceptedAt,
                    :textSnapshot
                )
                """)
                .param("id", UUID.randomUUID())
                .param("organizationId", invitation.organizationId())
                .param("patientId", patientId)
                .param("userId", userId)
                .param("invitationId", invitation.id())
                .param("purpose", CONSENT_PURPOSE)
                .param("textVersion", CONSENT_TEXT_VERSION)
                .param("acceptedAt", OffsetDateTime.ofInstant(acceptedAt, ZoneOffset.UTC))
                .param("textSnapshot", CONSENT_TEXT)
                .update();

        auditService.record(
                invitation.organizationId(),
                externalSubject,
                "PATIENT_INVITATION_ACCEPTED",
                "PATIENT_PERSON",
                patientId);
        recordOutboxEvent(
                invitation.organizationId(),
                "CARE_RELATIONSHIP_ACTIVATED",
                "PATIENT_PERSON",
                patientId,
                "{\"invitationId\":\"" + invitation.id() + "\"}");

        return new PatientInvitationAcceptanceResponse(
                patientId,
                "ACTIVE",
                acceptedAt);
    }

    private UUID findPatientPersonId(String email) {
        return jdbc.sql("""
                SELECT p.id
                FROM patient_person p
                JOIN app_user u ON u.id = p.user_id
                WHERE lower(u.email) = :email
                """)
                .param("email", email)
                .query(UUID.class)
                .optional()
                .orElse(null);
    }

    private UUID linkPatientIdentity(InvitationRow invitation, UUID userId) {
        UUID linkedPatientId = jdbc.sql("""
                SELECT id
                FROM patient_person
                WHERE user_id = :userId
                FOR UPDATE
                """)
                .param("userId", userId)
                .query(UUID.class)
                .optional()
                .orElse(null);
        if (linkedPatientId == null) {
            int linkedPatient = jdbc.sql("""
                    UPDATE patient_person
                    SET user_id = :userId
                    WHERE id = :patientId
                      AND user_id IS NULL
                    """)
                    .param("userId", userId)
                    .param("patientId", invitation.patientPersonId())
                    .update();
            if (linkedPatient != 1) {
                throw new PatientInvitationConflictException(
                        "This patient record is already linked to another identity.");
            }
            return invitation.patientPersonId();
        }
        if (linkedPatientId.equals(invitation.patientPersonId())) {
            return linkedPatientId;
        }

        try {
            int movedRelationship = jdbc.sql("""
                    UPDATE care_relationship
                    SET patient_person_id = :linkedPatientId
                    WHERE organization_id = :organizationId
                      AND patient_person_id = :invitedPatientId
                      AND status = 'INVITED'
                    """)
                    .param("linkedPatientId", linkedPatientId)
                    .param("organizationId", invitation.organizationId())
                    .param("invitedPatientId", invitation.patientPersonId())
                    .update();
            if (movedRelationship != 1) {
                throw new PatientInvitationConflictException(
                        "The care relationship cannot be linked to this identity.");
            }
        } catch (DataIntegrityViolationException exception) {
            throw new PatientInvitationConflictException(
                    "A care relationship already exists for this patient in the organization.");
        }
        jdbc.sql("""
                UPDATE patient_invitation
                SET patient_person_id = :linkedPatientId
                WHERE id = :invitationId
                """)
                .param("linkedPatientId", linkedPatientId)
                .param("invitationId", invitation.id())
                .update();
        jdbc.sql("DELETE FROM patient_person WHERE id = :invitedPatientId AND user_id IS NULL")
                .param("invitedPatientId", invitation.patientPersonId())
                .update();
        return linkedPatientId;
    }

    private void expireOlderPendingInvitation(UUID organizationId, String email) {
        jdbc.sql("""
                UPDATE patient_invitation
                SET status = 'EXPIRED'
                WHERE organization_id = :organizationId
                  AND lower(email) = :email
                  AND status = 'PENDING'
                  AND expires_at <= CURRENT_TIMESTAMP
                """)
                .param("organizationId", organizationId)
                .param("email", email)
                .update();
    }

    private InvitationRow findInvitation(String token, boolean lock) {
        String suffix = lock ? " FOR UPDATE" : "";
        return jdbc.sql("""
                SELECT
                    pi.id,
                    pi.organization_id,
                    pi.patient_person_id,
                    pi.accepted_by_user_id,
                    pi.email,
                    pi.status,
                    pi.expires_at,
                    pi.accepted_at,
                    o.name AS organization_name,
                    p.display_name AS patient_display_name
                FROM patient_invitation pi
                JOIN organization o ON o.id = pi.organization_id
                JOIN patient_person p ON p.id = pi.patient_person_id
                WHERE pi.token_hash = :tokenHash
                """ + suffix)
                .param("tokenHash", hashToken(token))
                .query((rs, rowNum) -> new InvitationRow(
                        rs.getObject("id", UUID.class),
                        rs.getObject("organization_id", UUID.class),
                        rs.getObject("patient_person_id", UUID.class),
                        rs.getObject("accepted_by_user_id", UUID.class),
                        rs.getString("email"),
                        rs.getString("status"),
                        rs.getTimestamp("expires_at").toInstant(),
                        rs.getTimestamp("accepted_at") == null
                                ? null
                                : rs.getTimestamp("accepted_at").toInstant(),
                        rs.getString("organization_name"),
                        rs.getString("patient_display_name")))
                .optional()
                .orElseThrow(PatientInvitationNotFoundException::new);
    }

    private UUID resolveOrCreatePatientUser(String externalSubject, String email, String displayName) {
        ExistingUser existing = jdbc.sql("""
                SELECT id, external_subject, email
                FROM app_user
                WHERE external_subject = :externalSubject
                   OR lower(email) = :email
                FOR UPDATE
                """)
                .param("externalSubject", externalSubject)
                .param("email", email)
                .query((rs, rowNum) -> new ExistingUser(
                        rs.getObject("id", UUID.class),
                        rs.getString("external_subject"),
                        rs.getString("email")))
                .optional()
                .orElse(null);
        if (existing != null) {
            if (!existing.externalSubject().equals(externalSubject)
                    || !existing.email().equalsIgnoreCase(email)) {
                throw new PatientInvitationIdentityMismatchException();
            }
            return existing.id();
        }

        UUID userId = UUID.randomUUID();
        try {
            jdbc.sql("""
                    INSERT INTO app_user (id, external_subject, email, display_name)
                    VALUES (:id, :externalSubject, :email, :displayName)
                    """)
                    .param("id", userId)
                    .param("externalSubject", externalSubject)
                    .param("email", email)
                    .param("displayName", displayName)
                    .update();
        } catch (DataIntegrityViolationException exception) {
            throw new PatientInvitationIdentityMismatchException();
        }
        return userId;
    }

    private UUID findUserId(String externalSubject, String email) {
        return jdbc.sql("""
                SELECT id
                FROM app_user
                WHERE external_subject = :externalSubject
                  AND lower(email) = :email
                """)
                .param("externalSubject", externalSubject)
                .param("email", email)
                .query(UUID.class)
                .optional()
                .orElseThrow(PatientInvitationNotFoundException::new);
    }

    private void recordOutboxEvent(
            UUID organizationId,
            String eventType,
            String aggregateType,
            UUID aggregateId,
            String payload) {
        jdbc.sql("""
                INSERT INTO outbox_event (
                    id, organization_id, event_type, aggregate_type, aggregate_id, payload
                ) VALUES (
                    :id, :organizationId, :eventType, :aggregateType, :aggregateId,
                    CAST(:payload AS JSONB)
                )
                """)
                .param("id", UUID.randomUUID())
                .param("organizationId", organizationId)
                .param("eventType", eventType)
                .param("aggregateType", aggregateType)
                .param("aggregateId", aggregateId)
                .param("payload", payload)
                .update();
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static String normalizeNullable(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }

    private static String generateToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String hashToken(String token) {
        if (token == null || token.isBlank()) throw new PatientInvitationNotFoundException();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }

    private static String maskEmail(String email) {
        int separator = email.indexOf('@');
        if (separator < 1) return "***";
        String local = email.substring(0, separator);
        String domain = email.substring(separator + 1);
        String visible = local.substring(0, 1);
        return visible + "***@" + domain;
    }

    private record InvitationRow(
            UUID id,
            UUID organizationId,
            UUID patientPersonId,
            UUID acceptedByUserId,
            String email,
            String status,
            Instant expiresAt,
            Instant acceptedAt,
            String organizationName,
            String patientDisplayName) {
    }

    private record ExistingUser(UUID id, String externalSubject, String email) {
    }
}
