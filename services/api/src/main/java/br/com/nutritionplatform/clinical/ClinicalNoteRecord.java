package br.com.nutritionplatform.clinical;

import java.time.Instant;
import java.util.UUID;

public record ClinicalNoteRecord(
        UUID id,
        int version,
        String status,
        String subjective,
        String objective,
        String assessment,
        String agreedActions,
        String amendmentReason,
        Instant updatedAt,
        Instant finalizedAt) {
}
