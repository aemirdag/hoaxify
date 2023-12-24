package com.hoaxify.ws.auth.token;

import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
public class BasicAuthTokenService implements TokenService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public BasicAuthTokenService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Token createToken(User user, Credentials credentials) {
        String emailColonPassword = credentials.email() + ":" + credentials.password();
        String token = Base64.getEncoder().encodeToString(emailColonPassword.getBytes());
        return Token.createToken("Basic", token);
    }

    @Override
    public User verifyToken(String authorizationHeader) {
        if (Objects.isNull(authorizationHeader)) {
            return null;
        }

        String base64Encoded = authorizationHeader.split(" ")[1];
        String decoded = new String(Base64.getDecoder().decode(base64Encoded));
        String[] emailColonPasswordArr = decoded.split(":");
        String email = emailColonPasswordArr[0];
        String password = emailColonPasswordArr[1];

        Optional<User> inDBOpt = userService.findByEmail(email);
        if (inDBOpt.isEmpty()) {
            return null;
        }
        User inDB = inDBOpt.get();

        if (!passwordEncoder.matches(password, inDB.getPassword())) {
            return null;
        }

        return inDB;
    }
}
