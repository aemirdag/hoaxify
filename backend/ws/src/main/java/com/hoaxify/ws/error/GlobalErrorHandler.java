package com.hoaxify.ws.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.HashMap;

@ControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DisabledException.class)
    ResponseEntity<Error> handleDisabledException(DisabledException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(constructError(e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class) // for method level authorization
    ResponseEntity<Error> handleAccessDeniedException(DisabledException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(constructError(e.getMessage(), request.getRequestURI()));
    }

    private Error constructError(String message, String path) {
        return Error.builder()
                .message(message)
                .path(path)
                .timestamp(new Date().getTime())
                .validationErrors(new HashMap<>())
                .build();
    }
}
