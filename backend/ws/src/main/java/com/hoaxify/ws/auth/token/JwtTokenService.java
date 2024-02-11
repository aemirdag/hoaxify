package com.hoaxify.ws.auth.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Objects;

@Service
@Primary
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
            return new Token("Bearer", token);
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
            long userID = Long.parseLong(claimsJws.getPayload().getSubject());
            User user = new User();
            user.setId(userID);

            return user;
        } catch (JwtException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static record TokenSubject(long id, boolean active) {}
}
