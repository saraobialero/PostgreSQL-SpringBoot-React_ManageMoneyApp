package com.app.manage_money.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecurringTransactionException extends BaseException {
    public RecurringTransactionException(String message, String details) {
        super(message, details);
    }
}
