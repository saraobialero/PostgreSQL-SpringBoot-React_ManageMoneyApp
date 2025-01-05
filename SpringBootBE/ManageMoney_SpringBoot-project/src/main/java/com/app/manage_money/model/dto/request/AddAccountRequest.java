package com.app.manage_money.model.dto.request;

import com.app.manage_money.model.enums.AccountType;
import com.app.manage_money.model.enums.State;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddAccountRequest {
        @NotNull(message = "Account type is required")
        private AccountType accountType;

        private State state;

        @NotNull(message = "Balance is required")
        private BigDecimal balance;
}
