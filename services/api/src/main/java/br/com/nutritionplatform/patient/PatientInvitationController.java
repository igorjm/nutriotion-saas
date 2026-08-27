package br.com.nutritionplatform.patient;

import jakarta.validation.Valid;
import java.net.URI;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patient-invitations")
public class PatientInvitationController {
    private final PatientInvitationService service;

    public PatientInvitationController(PatientInvitationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PatientInvitationCreated> create(
            @Valid @RequestBody CreatePatientInvitationRequest request,
            Principal principal) {
        PatientInvitationCreated response = service.create(principal.getName(), request);
        return ResponseEntity
                .created(URI.create("/api/v1/patient-invitations/" + response.invitationId()))
                .body(response);
    }

    @PostMapping("/{token}/accept")
    public PatientInvitationAcceptanceResponse accept(
            @PathVariable String token,
            @Valid @RequestBody PatientInvitationAcceptanceRequest request,
            Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthentication)) {
            throw new PatientInvitationIdentityMismatchException();
        }
        String email = jwtAuthentication.getToken().getClaimAsString("email");
        if (email == null || email.isBlank()) {
            throw new PatientInvitationIdentityMismatchException();
        }
        return service.accept(token, authentication.getName(), email, request);
    }
}
