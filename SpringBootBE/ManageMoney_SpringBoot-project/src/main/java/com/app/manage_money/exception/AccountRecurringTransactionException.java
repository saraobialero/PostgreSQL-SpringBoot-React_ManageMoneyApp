package com.app.manage_money.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Setter
@Getter
public class AccountRecurringTransactionException extends RuntimeException {
    private final ErrorResponse response;

    @Override
    public String getMessage() {
        return response.getMessage();
    }
}
