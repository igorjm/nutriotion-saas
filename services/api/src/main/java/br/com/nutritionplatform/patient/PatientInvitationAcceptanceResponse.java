package br.com.nutritionplatform.patient;

import java.time.Instant;
import java.util.UUID;

public record PatientInvitationAcceptanceResponse(
        UUID patientId,
        String relationshipStatus,
        Instant acceptedAt) {
}
