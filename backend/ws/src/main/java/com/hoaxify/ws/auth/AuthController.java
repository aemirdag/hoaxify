package com.hoaxify.ws.auth;

import com.hoaxify.ws.auth.dto.AuthResponse;
import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.shared.GenericMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
    public GenericMessage handleLogout(@RequestHeader(name = "Authorization", required = false)
                                       String authorizationHeader) {
        authService.logout(authorizationHeader);

        return new GenericMessage("Logout success");
    }
}
