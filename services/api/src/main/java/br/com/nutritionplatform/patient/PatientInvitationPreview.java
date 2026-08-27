package br.com.nutritionplatform.patient;

import java.time.Instant;

public record PatientInvitationPreview(
        String organizationName,
        String patientDisplayName,
        String maskedEmail,
        String status,
        Instant expiresAt) {
}
