package br.com.nutritionplatform.clinical;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patients/{patientId}")
public class ClinicalRecordController {
    private final ClinicalRecordService service;

    public ClinicalRecordController(ClinicalRecordService service) {
        this.service = service;
    }

    @GetMapping("/clinical-record")
    public PatientClinicalRecord getRecord(
            @PathVariable UUID patientId,
            Principal principal) {
        return service.getRecord(principal.getName(), patientId);
    }

    @PutMapping("/intake")
    public PatientIntakeRecord updateIntake(
            @PathVariable UUID patientId,
            @Valid @RequestBody UpdatePatientIntakeRequest request,
            Principal principal) {
        return service.updateIntake(principal.getName(), patientId, request);
    }

    @PostMapping("/consultations")
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultationWorkspace startConsultation(
            @PathVariable UUID patientId,
            Principal principal) {
        return service.startConsultation(principal.getName(), patientId);
    }

    @PutMapping("/consultations/{consultationId}/note")
    public ConsultationWorkspace saveDraft(
            @PathVariable UUID patientId,
            @PathVariable UUID consultationId,
            @Valid @RequestBody UpdateClinicalNoteRequest request,
            Principal principal) {
        return service.saveDraft(principal.getName(), patientId, consultationId, request);
    }

    @PostMapping("/consultations/{consultationId}/finalize")
    public ConsultationWorkspace finalizeNote(
            @PathVariable UUID patientId,
            @PathVariable UUID consultationId,
            Principal principal) {
        return service.finalizeNote(principal.getName(), patientId, consultationId);
    }

    @PostMapping("/consultations/{consultationId}/amendments")
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultationWorkspace startAmendment(
            @PathVariable UUID patientId,
            @PathVariable UUID consultationId,
            @Valid @RequestBody CreateAmendmentRequest request,
            Principal principal) {
        return service.startAmendment(principal.getName(), patientId, consultationId, request);
    }
}
