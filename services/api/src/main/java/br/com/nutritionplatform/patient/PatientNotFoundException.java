package br.com.nutritionplatform.patient;

import java.util.UUID;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(UUID patientId) {
        super("Patient was not found: " + patientId);
    }
}
