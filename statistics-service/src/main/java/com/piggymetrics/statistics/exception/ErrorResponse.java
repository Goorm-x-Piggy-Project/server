package com.piggymetrics.statistics.exception;

public class ErrorResponse {
    private String errorCode;
    private String message;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    // Getters and setters
    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
