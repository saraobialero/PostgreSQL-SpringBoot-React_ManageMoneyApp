package com.app.manage_money.model.dto.request;


import com.app.manage_money.model.enums.Frequency;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AddRecurringTransactionRequest {


    private LabelType labelType;

    @NotNull(message = "Source account ID is required")
    private Integer sourceAccountId;

    @NotNull
    private TransactionType transactionType;


    @NotNull
    @Positive
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private Frequency frequency;

    @NotNull
    @PastOrPresent
    private LocalDate startDate;

    @NotNull
    @FutureOrPresent
    private LocalDate endDate;

    @FutureOrPresent
    private LocalDate nextOccurrence;

    private String beneficiary;
    private String source;

    @NotNull
    private boolean isActive;

    private Integer destinationAccountId;
}