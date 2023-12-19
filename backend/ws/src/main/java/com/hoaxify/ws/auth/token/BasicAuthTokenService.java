package com.hoaxify.ws.auth.token;

import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.user.User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class BasicAuthTokenService implements TokenService {
    @Override
    public Token createToken(User user, Credentials credentials) {
        String emailColonPassword = credentials.email() + ":" + credentials.password();
        String token = Base64.getEncoder().encodeToString(emailColonPassword.getBytes());
        return Token.getToken("Basic", token);
    }

    @Override
    public User verifyToken(String authorizationHeader) {
        return null;
    }
}
