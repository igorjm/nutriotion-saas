package br.com.nutritionplatform.clinical;

import java.time.Instant;

public record PatientIntakeRecord(
        String allergies,
        String foodRestrictions,
        String clinicalHistory,
        String routineNotes,
        String careGoal,
        int version,
        Instant updatedAt) {
}
