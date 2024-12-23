package com.app.manage_money.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TransactionException extends BaseException {
    public TransactionException(String message, String details) {
        super(message, details);
    }
}
