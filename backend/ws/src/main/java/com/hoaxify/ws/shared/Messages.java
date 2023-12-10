package com.hoaxify.ws.shared;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;

public class Messages {
    public static String getMessageForLocale(String messageKey) {
        return ResourceBundle.getBundle("Messages", LocaleContextHolder.getLocale()).getString(messageKey);
    }
}
