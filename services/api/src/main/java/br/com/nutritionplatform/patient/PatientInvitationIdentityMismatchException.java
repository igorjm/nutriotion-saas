package br.com.nutritionplatform.patient;

public class PatientInvitationIdentityMismatchException extends RuntimeException {
    public PatientInvitationIdentityMismatchException() {
        super("The authenticated identity does not match this invitation.");
    }
}
