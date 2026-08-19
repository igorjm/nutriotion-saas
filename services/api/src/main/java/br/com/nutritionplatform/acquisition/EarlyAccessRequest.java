package br.com.nutritionplatform.acquisition;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EarlyAccessRequest(
        @NotBlank @Size(min = 2, max = 120) String name,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(min = 2, max = 80) String currentTool,
        @NotBlank @Size(min = 2, max = 120) String source,
        boolean marketingConsent,
        @NotBlank @Pattern(regexp = "^[a-z0-9-]{3,60}$") String consentTextVersion) {
}
