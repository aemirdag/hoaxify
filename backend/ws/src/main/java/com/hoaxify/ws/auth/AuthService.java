package com.hoaxify.ws.auth;

import com.hoaxify.ws.auth.dto.AuthResponse;
import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.auth.exception.AuthenticationException;
import com.hoaxify.ws.auth.token.Token;
import com.hoaxify.ws.auth.token.TokenService;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserService;
import com.hoaxify.ws.user.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthService(UserService userService, TokenService tokenService,
                       PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public AuthResponse authenticate(Credentials credentials) {
        User inDB = userService.findByEmail(credentials.email()).orElseThrow(AuthenticationException::new);

        if (!passwordEncoder.matches(credentials.password(), inDB.getPassword())) {
            throw new AuthenticationException();
        }

        Token token = tokenService.createToken(inDB, credentials);

        return AuthResponse.getAuthResponse(modelMapper.map(inDB, UserDTO.class), token);
    }

    public void logout(String authorizationHeader) {
        tokenService.logout(authorizationHeader);
    }
}
