package com.hoaxify.ws.auth.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Objects;

@Service
@ConditionalOnProperty(name = "hoaxify.token-type", havingValue = "jwt")
public class JwtTokenService implements TokenService {
    private final SecretKey secretKey;
    private final ObjectMapper mapper;

    @Autowired
    public JwtTokenService(ObjectMapper mapper) {
        this.mapper = mapper;
        secretKey = Keys.hmacShaKeyFor("secret-must-be-at-least-32-characters".getBytes());
    }

    @Override
    public Token createToken(User user, Credentials credentials) {
        TokenSubject tokenSubject = new TokenSubject(user.getId(), user.isActive());
        try {
            String subject = mapper.writeValueAsString(tokenSubject);
            String token = Jwts.builder()
                    .subject(subject).signWith(secretKey).compact();
            return new Token(token, "Bearer");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User verifyToken(String authorizationHeader) {
        if (Objects.isNull(authorizationHeader) || authorizationHeader.isEmpty()) {
            return null;
        }

        String token = authorizationHeader.split(" ")[1];

        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();

        try {
            Jws<Claims> claimsJws = jwtParser.parseSignedClaims(token);
            String subject = claimsJws.getPayload().getSubject();
            var tokenSubject = mapper.readValue(subject, TokenSubject.class);

            User user = new User();
            user.setId(tokenSubject.id());
            user.setActive(tokenSubject.active());

            return user;
        } catch (JwtException | JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static record TokenSubject(long id, boolean active) {}
}
