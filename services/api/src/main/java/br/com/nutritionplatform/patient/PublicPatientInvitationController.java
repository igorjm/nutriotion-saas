package br.com.nutritionplatform.patient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/patient-invitations")
public class PublicPatientInvitationController {
    private final PatientInvitationService service;

    public PublicPatientInvitationController(PatientInvitationService service) {
        this.service = service;
    }

    @GetMapping("/{token}")
    public PatientInvitationPreview preview(@PathVariable String token) {
        return service.preview(token);
    }
}
