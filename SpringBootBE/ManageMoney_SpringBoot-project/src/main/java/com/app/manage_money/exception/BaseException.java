package com.app.manage_money.exception;

import java.util.Date;

public class BaseException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public BaseException(String message, String details) {
        super(message);
        this.errorResponse = new ErrorResponse(
                new Date(),
                message,
                details
        );
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}