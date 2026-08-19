package br.com.nutritionplatform.patient;

import java.util.UUID;

public record PatientSummary(UUID id, String displayName, String relationshipStatus) {
}
