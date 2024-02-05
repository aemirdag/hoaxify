package com.hoaxify.ws.shared;

import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Messages {
    public static String getMessageForLocale(String messageKey) {
        return ResourceBundle.getBundle("Messages", LocaleContextHolder.getLocale()).getString(messageKey);
    }

    public static String getMessageForLocale(String messageKey, Object... args) {
        return MessageFormat.format(getMessageForLocale(messageKey), args);
    }

    public static String getValidationMessageForLocale(String messageKey) {
        return ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale()).getString(messageKey);
    }

    public static String getValidationMessageForLocale(String messageKey, Object... args) {
        return MessageFormat.format(getValidationMessageForLocale(messageKey), args);
    }
}
