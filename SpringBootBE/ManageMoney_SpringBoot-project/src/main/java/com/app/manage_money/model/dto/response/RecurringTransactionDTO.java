package com.app.manage_money.model.dto.response;

import com.app.manage_money.model.enums.Frequency;
import com.app.manage_money.model.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class RecurringTransactionDTO {
    private Integer id;
    private LabelDTO label;
    private TransactionType transactionType;
    private BigDecimal amount;
    private Frequency frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextOccurrence;
    private String beneficiary;
    private String source;
    private LocalDate lastProcessedDate;
    private boolean isActive;
}