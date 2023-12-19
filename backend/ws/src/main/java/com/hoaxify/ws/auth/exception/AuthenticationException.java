package com.hoaxify.ws.auth.exception;

import com.hoaxify.ws.shared.Messages;

public class AuthenticationException extends RuntimeException{
    public AuthenticationException() {
        super(Messages.getMessageForLocale("hoaxify.auth.invalid.credentials"));
    }
}
