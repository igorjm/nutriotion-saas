package br.com.nutritionplatform.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PatientInvitationAcceptanceRequest(
        @NotBlank @Pattern(regexp = "^[a-z0-9-]{3,60}$") String consentTextVersion) {
}
