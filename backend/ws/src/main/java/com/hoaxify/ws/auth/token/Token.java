package com.hoaxify.ws.auth.token;

public record Token(String prefix, String token) {
    public static Token createToken(String prefix, String token) {
        return new Token(prefix, token);
    }
}
