package br.com.nutritionplatform.patient;

public class PatientInvitationConflictException extends RuntimeException {
    public PatientInvitationConflictException(String message) {
        super(message);
    }
}
