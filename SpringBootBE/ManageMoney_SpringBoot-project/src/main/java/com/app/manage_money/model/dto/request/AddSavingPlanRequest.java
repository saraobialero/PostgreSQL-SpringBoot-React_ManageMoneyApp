package com.app.manage_money.model.dto.request;

import com.app.manage_money.model.enums.AccountType;
import jakarta.validation.constraints.*;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AddSavingPlanRequest {
    @NotNull(message = "Account id is required")
    private Integer accountId;

    private BigDecimal targetAmount;

    @NotNull
    @Positive
    @DecimalMin(value = "0.01")
    private BigDecimal currentAmount;

    @NotNull
    @PastOrPresent
    private LocalDate startDate;

    @FutureOrPresent
    private LocalDate targetDate;
}
