package br.com.nutritionplatform.clinical;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePatientIntakeRequest(
        @NotNull @Size(max = 2000) String allergies,
        @NotNull @Size(max = 2000) String foodRestrictions,
        @NotNull @Size(max = 5000) String clinicalHistory,
        @NotNull @Size(max = 5000) String routineNotes,
        @NotNull @Size(max = 2000) String careGoal) {
}
