package com.hoaxify.ws.auth.dto;

import com.hoaxify.ws.auth.token.Token;
import com.hoaxify.ws.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {
    private UserDTO user;
    private Token token;

    public static AuthResponse getAuthResponse(UserDTO userDTO, Token token) {
        return new AuthResponse(userDTO, token);
    }
}
