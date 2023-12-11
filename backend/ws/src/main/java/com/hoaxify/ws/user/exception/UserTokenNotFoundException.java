package com.hoaxify.ws.user.exception;

import com.hoaxify.ws.shared.Messages;

public class UserTokenNotFoundException extends RuntimeException {
    private final String token;

    public UserTokenNotFoundException(String token) {
        super(Messages.getMessageForLocale("hoaxify.create.user.token.find.failure"));
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
