package com.hoaxify.ws.user.dto;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.validation.UniqueEmail;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreate(
        @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$",
                message = "{hoaxify.constraint.username.pattern}")
        String username,

        @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
                message = "{hoaxify.constraint.email.pattern}")
        @UniqueEmail
        String email,

        @Size(min=4, max=255)
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "{hoaxify.constraint.password.pattern}")
        String password
) {
    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }
}
