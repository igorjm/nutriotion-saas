package br.com.nutritionplatform.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePatientInvitationRequest(
        @NotBlank @Size(min = 2, max = 160) String displayName,
        @NotBlank @Email @Size(max = 254) String email,
        @Size(max = 120) String careFocus) {
}
