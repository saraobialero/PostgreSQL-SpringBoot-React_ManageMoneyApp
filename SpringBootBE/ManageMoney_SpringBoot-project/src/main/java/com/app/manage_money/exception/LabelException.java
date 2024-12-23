package com.app.manage_money.exception;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LabelException extends BaseException {
    public LabelException(String message, String details) {
        super(message, details);
    }
}