package br.com.nutritionplatform.platform.config;

import br.com.nutritionplatform.identity.MembershipNotFoundException;
import br.com.nutritionplatform.patient.PatientNotFoundException;
import br.com.nutritionplatform.patient.PatientInvitationConflictException;
import br.com.nutritionplatform.patient.PatientInvitationIdentityMismatchException;
import br.com.nutritionplatform.patient.PatientInvitationNotFoundException;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(MembershipNotFoundException.class)
    ProblemDetail membershipNotFound() {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "The authenticated identity has no active organization context.");
        problem.setType(URI.create("https://errors.product.invalid/membership-required"));
        problem.setTitle("Organization membership required");
        return problem;
    }

    @ExceptionHandler(PatientNotFoundException.class)
    ProblemDetail patientNotFound() {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The patient does not exist or is not accessible in the active organization.");
        problem.setType(URI.create("https://errors.product.invalid/patient-not-found"));
        problem.setTitle("Patient not found");
        return problem;
    }

    @ExceptionHandler(PatientInvitationNotFoundException.class)
    ProblemDetail patientInvitationNotFound() {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The invitation does not exist, has expired, or is no longer available.");
        problem.setType(URI.create("https://errors.product.invalid/patient-invitation-not-found"));
        problem.setTitle("Patient invitation not found");
        return problem;
    }

    @ExceptionHandler(PatientInvitationConflictException.class)
    ProblemDetail patientInvitationConflict(PatientInvitationConflictException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                exception.getMessage());
        problem.setType(URI.create("https://errors.product.invalid/patient-invitation-conflict"));
        problem.setTitle("Patient invitation conflict");
        return problem;
    }

    @ExceptionHandler(PatientInvitationIdentityMismatchException.class)
    ProblemDetail patientInvitationIdentityMismatch() {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "The authenticated account cannot accept this invitation.");
        problem.setType(URI.create("https://errors.product.invalid/patient-invitation-identity"));
        problem.setTitle("Patient invitation identity mismatch");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validation(MethodArgumentNotValidException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "One or more request fields are invalid.");
        problem.setType(URI.create("https://errors.product.invalid/validation"));
        problem.setTitle("Request validation failed");
        List<String> fields = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField())
                .distinct()
                .sorted()
                .toList();
        problem.setProperty("invalidFields", fields);
        return problem;
    }
}
