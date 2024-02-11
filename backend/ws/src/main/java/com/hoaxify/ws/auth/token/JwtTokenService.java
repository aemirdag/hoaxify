package com.hoaxify.ws.auth.token;

import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Objects;

@Service
@Primary
public class JwtTokenService implements TokenService {
    private final SecretKey secretKey;

    public JwtTokenService() {
        secretKey = Keys.hmacShaKeyFor("secret-must-be-at-least-32-characters".getBytes());
    }

    @Override
    public Token createToken(User user, Credentials credentials) {
        String token = Jwts.builder()
                .subject(Long.toString(user.getId())).signWith(secretKey).compact();

        return new Token("Bearer", token);
    }

    @Override
    public User verifyToken(String authorizationHeader) {
        if (Objects.isNull(authorizationHeader) || authorizationHeader.isEmpty()) {
            return null;
        }

        String token = authorizationHeader.split("Bearer ")[1];

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
}
