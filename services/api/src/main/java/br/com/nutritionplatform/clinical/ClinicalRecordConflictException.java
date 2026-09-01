package br.com.nutritionplatform.clinical;

public class ClinicalRecordConflictException extends RuntimeException {
    public ClinicalRecordConflictException(String message) {
        super(message);
    }
}
