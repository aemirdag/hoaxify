package com.hoaxify.ws.error;

import com.hoaxify.ws.auth.exception.AuthenticationException;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.exception.ActivationNotificationException;
import com.hoaxify.ws.user.exception.AuthorizationException;
import com.hoaxify.ws.user.exception.UserNotFoundException;
import com.hoaxify.ws.user.exception.UserTokenNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(constructError(e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleMethodArgNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        Error error = constructError(Messages.getMessageForLocale("hoaxify.error.validation"),
                request.getRequestURI());

        for (var fieldError : e.getBindingResult().getFieldErrors()) {
            error.putError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ActivationNotificationException.class)
    public ResponseEntity<Error> handleActivationNotificationException(ActivationNotificationException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(constructError(e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(UserTokenNotFoundException.class)
    public ResponseEntity<Error> handleUserTokenNotFoundException(UserTokenNotFoundException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(constructError(e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(constructError(e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Error> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(constructError(e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Error> handleAuthorizationException(AuthorizationException e, HttpServletRequest request) {
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
