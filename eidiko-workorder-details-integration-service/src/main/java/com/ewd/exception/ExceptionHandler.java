package com.ewd.exception;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ExceptionHandler {

    public static final String DESCRIPTION = "description";



    public static ProblemDetail handleSecurityException(Exception exception) {
        ProblemDetail errorDetail = null;
        exception.printStackTrace();

        // Handle known exceptions and create corresponding error details
        if (exception instanceof BadCredentialsException) {
            log.error("BadCredentialsException {}", exception.getMessage());
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            errorDetail.setProperty(DESCRIPTION, "The username or password is incorrect");
            return errorDetail;
        }

        if (exception instanceof AccountStatusException) {
            log.error("AccountStatusException {}", exception.getMessage());
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty(DESCRIPTION, "The account is locked");
        }

        if (exception instanceof AccessDeniedException) {
            log.error("AccessDeniedException {}", exception.getMessage());
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty(DESCRIPTION, "You are not authorized to access this resource");
        }

        if (exception instanceof SignatureException) {
            log.error("SignatureException {}", exception.getMessage());
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty(DESCRIPTION, "The JWT signature is invalid");
        }

        if (exception instanceof ExpiredJwtException) {
            log.error("ExpiredJwtException {}", exception.getMessage());
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty(DESCRIPTION, "The JWT token has expired");
        }

        if (exception instanceof UserAlreadyExist) {
            log.error("UserAlreadyExist {}", exception.getMessage());
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), exception.getMessage());
            errorDetail.setProperty(DESCRIPTION, "User details exist already, please try with a different id or email");
        }

        // Default error detail in case no match found
        if (errorDetail == null) {
            log.error("Unknown security exception: {}", exception.getMessage());
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), "An unknown error occurred during authentication.");
            errorDetail.setProperty(DESCRIPTION, "An unexpected error occurred.");
        }

        // Ensure errorDetail is never null before returning
        return errorDetail != null ? errorDetail : ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), "Unknown error occurred.");
    }
}
