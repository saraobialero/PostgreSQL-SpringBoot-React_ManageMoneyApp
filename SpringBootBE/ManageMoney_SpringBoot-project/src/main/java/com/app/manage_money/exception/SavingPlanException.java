package com.app.manage_money.exception;

import com.app.manage_money.model.dto.response.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Setter
@Getter
public class SavingPlanException extends RuntimeException {
    private ErrorResponse response;

    @Override
    public String getMessage() {
        return response.getMessage();
    }
}
