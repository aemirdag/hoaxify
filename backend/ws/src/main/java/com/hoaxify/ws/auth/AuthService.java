package com.hoaxify.ws.auth;

import com.hoaxify.ws.auth.dto.AuthResponse;
import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.auth.exception.AuthenticationException;
import com.hoaxify.ws.auth.token.Token;
import com.hoaxify.ws.auth.token.TokenService;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public AuthService(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public AuthResponse authenticate(Credentials credentials) {
        User inDB = userService.findByEmail(credentials.email()).orElseThrow(AuthenticationException::new);

        if (!passwordEncoder.matches(credentials.password(), inDB.getPassword())) {
            throw new AuthenticationException();
        }

        Token token = tokenService.createToken(inDB, credentials);

        return AuthResponse.getAuthResponse(userService.mapModel(inDB), token);
    }
}
