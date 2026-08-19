package br.com.nutritionplatform.patient;

import java.security.Principal;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {
    private final PatientQueryService service;

    public PatientController(PatientQueryService service) {
        this.service = service;
    }

    @GetMapping("/{patientId}")
    public PatientSummary get(@PathVariable UUID patientId, Principal principal) {
        return service.getForAuthenticatedSubject(principal.getName(), patientId);
    }
}
