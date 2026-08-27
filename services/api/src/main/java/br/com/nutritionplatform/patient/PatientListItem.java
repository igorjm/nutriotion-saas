package br.com.nutritionplatform.patient;

import java.time.Instant;
import java.util.UUID;

public record PatientListItem(
        UUID id,
        String displayName,
        String contactEmail,
        String careFocus,
        String relationshipStatus,
        Instant relationshipCreatedAt) {
}
