package com.app.manage_money.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class AccountException extends BaseException {
    public AccountException(String message, String details) {
        super(message, details);
    }
}
