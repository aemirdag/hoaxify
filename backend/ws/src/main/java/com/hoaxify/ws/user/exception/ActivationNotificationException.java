package com.hoaxify.ws.user.exception;

import com.hoaxify.ws.shared.Messages;

public class ActivationNotificationException extends RuntimeException {
    public ActivationNotificationException() {
        super(Messages.getMessageForLocale("hoaxify.create.user.mail.failure"));
    }
}
