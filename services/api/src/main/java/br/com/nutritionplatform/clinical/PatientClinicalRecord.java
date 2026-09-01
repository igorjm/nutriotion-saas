package br.com.nutritionplatform.clinical;

import java.util.UUID;

public record PatientClinicalRecord(
        UUID patientId,
        String displayName,
        String contactEmail,
        String careFocus,
        String relationshipStatus,
        PatientIntakeRecord intake,
        ConsultationWorkspace consultation) {
}
