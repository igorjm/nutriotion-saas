package br.com.nutritionplatform.patient;

import java.time.Instant;
import java.util.UUID;

public record PatientInvitationCreated(
        UUID invitationId,
        UUID patientId,
        String token,
        String status,
        Instant expiresAt) {
}
