package br.com.nutritionplatform.clinical;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateClinicalNoteRequest(
        @NotNull @Size(max = 10000) String subjective,
        @NotNull @Size(max = 10000) String objective,
        @NotNull @Size(max = 10000) String assessment,
        @NotNull @Size(max = 10000) String agreedActions) {
}
