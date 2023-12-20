package com.hoaxify.ws.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Builder
public class Error {
    @Setter
    private String message;

    private String path;
    private long timestamp;
    private final Map<String, String> validationErrors;

    public void putError(String key, String value) {
        validationErrors.put(key, value);
    }
}
