package com.app.manage_money.model.dto.request;

import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionUpdateRequest {
    @NotNull(message = "Label type is required")
    private LabelType labelType;

    @NotNull(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Size(max = 100, message = "Beneficiary must not exceed 100 characters")
    private String beneficiary;

    @Size(max = 100, message = "Source must not exceed 100 characters")
    private String source;

    @NotNull(message = "Recurring status is required")
    private Boolean isRecurring;

    @NotNull(message = "Transaction date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDate transactionDate;
}
