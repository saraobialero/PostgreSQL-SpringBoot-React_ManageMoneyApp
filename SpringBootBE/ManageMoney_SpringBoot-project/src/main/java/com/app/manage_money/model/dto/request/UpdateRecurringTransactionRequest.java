package com.app.manage_money.model.dto.request;


import com.app.manage_money.model.enums.Frequency;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateRecurringTransactionRequest {


    private LabelType labelType;

    private Integer sourceAccountId;

    private TransactionType transactionType;


    @Positive
    @DecimalMin(value = "0.01")
    private BigDecimal amount;


    private Frequency frequency;


    @PastOrPresent
    private LocalDate startDate;

    @FutureOrPresent
    private LocalDate endDate;

    @FutureOrPresent
    private LocalDate nextOccurrence;

    private String beneficiary;
    private String source;


    private boolean isActive;
}