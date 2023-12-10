package com.hoaxify.ws.error;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApiError {
    private int status;
    private String message;
    private String path;
    private long timestamp = new Date().getTime();
    private final Map<String, String> validationErrors = new HashMap<>();

    public static ApiError getApiError()  {
        return new ApiError();
    }

    public int getStatus() {
        return status;
    }

    public ApiError setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ApiError setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ApiError setPath(String path) {
        this.path = path;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ApiError setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public ApiError putError(String key, String value) {
        validationErrors.put(key, value);
        return this;
    }

    public int size() {
        return validationErrors.size();
    }
}
