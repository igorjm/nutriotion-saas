package br.com.nutritionplatform.patient;

public class PatientInvitationNotFoundException extends RuntimeException {
    public PatientInvitationNotFoundException() {
        super("The invitation does not exist or is no longer available.");
    }
}
