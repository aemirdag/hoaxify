package com.hoaxify.ws.user.exception;

import com.hoaxify.ws.shared.Messages;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super(Messages.getMessageForLocale("hoaxify.auth"));
    }
}
