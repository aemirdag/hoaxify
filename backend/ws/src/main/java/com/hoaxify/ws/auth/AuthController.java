package com.hoaxify.ws.auth;

import com.hoaxify.ws.auth.dto.AuthResponse;
import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.auth.exception.AuthenticationException;
import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shared.Messages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("api/v1/auth")
    ResponseEntity<AuthResponse> handleAuth(@Valid @RequestBody Credentials credentials) {
        AuthResponse authResponse = authService.authenticate(credentials);

        return ResponseEntity.ok(authResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        ApiError apiError = ApiError.getApiError()
                .setPath(request.getRequestURI())
                .setMessage(e.getMessage())
                .setStatus(HttpStatus.UNAUTHORIZED.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleMethodArgNotValidException(MethodArgumentNotValidException e) {
        ApiError apiError = ApiError.getApiError()
                .setPath("/api/v1/users")
                .setMessage(Messages.getMessageForLocale("hoaxify.error.validation"))
                .setStatus(HttpStatus.BAD_REQUEST.value());

        for (var fieldError : e.getBindingResult().getFieldErrors()) {
            apiError.putError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
