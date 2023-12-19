package com.hoaxify.ws.auth.token;

public record Token(String prefix, String token) {
    public static Token getToken(String prefix, String token) {
        return new Token(prefix, token);
    }
}
