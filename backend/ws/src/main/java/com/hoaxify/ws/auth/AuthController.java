package com.hoaxify.ws.auth;

import com.hoaxify.ws.auth.dto.AuthResponse;
import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.shared.GenericMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("api/v1/auth")
    public ResponseEntity<AuthResponse> handleAuthentication(@Valid @RequestBody Credentials credentials) {
        AuthResponse authResponse = authService.authenticate(credentials);
        ResponseCookie cookie = ResponseCookie
                .from("hoax-token", authResponse.getToken().getToken())
                .path("/")
                .httpOnly(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }

    @PostMapping("/api/v1/logout")
    public ResponseEntity<GenericMessage> handleLogout(@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
                                                       @CookieValue(name = "hoax-token", required = false) String cookieValue) {
        String tokenWithPrefix = authorizationHeader;
        if (Objects.nonNull(cookieValue) && !cookieValue.isEmpty()) {
            tokenWithPrefix = cookieValue;
        }

        authService.logout(tokenWithPrefix);

        ResponseCookie cookie = ResponseCookie
                .from("hoax-token", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new GenericMessage("Logout success"));
    }
}
