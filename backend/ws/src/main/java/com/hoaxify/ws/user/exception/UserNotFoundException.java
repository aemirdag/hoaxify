package com.hoaxify.ws.user.exception;

import com.hoaxify.ws.shared.Messages;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long id) {
        super(Messages.getMessageForLocale("hoaxify.user.id.not.found", id));
    }

    public UserNotFoundException(String email) {
        super(Messages.getMessageForLocale("hoaxify.user.email.not.found", email));
    }
}
