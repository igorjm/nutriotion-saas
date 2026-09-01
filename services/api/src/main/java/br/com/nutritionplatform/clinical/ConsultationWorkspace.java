package br.com.nutritionplatform.clinical;

import java.time.Instant;
import java.util.UUID;

public record ConsultationWorkspace(
        UUID id,
        String status,
        Instant createdAt,
        Instant finalizedAt,
        ClinicalNoteRecord note) {
}
