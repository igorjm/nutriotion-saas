package br.com.nutritionplatform.clinical;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAmendmentRequest(
        @NotBlank @Size(min = 5, max = 500) String reason) {
}
