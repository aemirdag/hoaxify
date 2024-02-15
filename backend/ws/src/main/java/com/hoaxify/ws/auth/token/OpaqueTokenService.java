package com.hoaxify.ws.auth.token;

import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "hoaxify.token-type", havingValue = "opaque")
public class OpaqueTokenService implements TokenService {
    private final TokenRepository tokenRepository;

    @Autowired
    public OpaqueTokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token createToken(User user, Credentials credentials) {
        String randomValue = UUID.randomUUID().toString();

        return tokenRepository.save(new Token(randomValue, user));
    }

    @Override
    public User verifyToken(String authorizationHeader) {
        if (Objects.isNull(authorizationHeader) || authorizationHeader.isEmpty()) {
            return null;
        }

        String token = authorizationHeader.split(" ")[1];
        Optional<Token> tokenInDB = tokenRepository.findById(token);
        return tokenInDB.map(Token::getUser).orElse(null);
    }
}
