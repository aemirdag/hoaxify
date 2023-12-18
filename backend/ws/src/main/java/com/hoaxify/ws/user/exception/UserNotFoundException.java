package com.hoaxify.ws.user.exception;

import com.hoaxify.ws.shared.Messages;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final long id;

    public UserNotFoundException(long id) {
        super(Messages.getMessageForLocale("hoaxify.user.not.found", id));
        this.id = id;
    }
}
