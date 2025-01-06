package com.app.manage_money.model.dto.request;


import com.app.manage_money.model.enums.LabelType;

import com.app.manage_money.model.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class AddTransactionRequest {

    private LabelType labelType;

    @NotNull(message = "Account ID is required")
    private Integer accountId;

    @NotNull
    private String name;

    @NotNull
    private TransactionType transactionType;

    private String location;

    @NotNull
    @Positive
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    private String beneficiary;

    private String source;

    private boolean isRecurring;

    @NotNull
    @PastOrPresent
    private LocalDate transactionDate;
}