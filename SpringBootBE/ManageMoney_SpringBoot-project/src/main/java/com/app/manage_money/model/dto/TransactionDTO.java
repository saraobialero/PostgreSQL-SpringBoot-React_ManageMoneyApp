package com.app.manage_money.model.dto;

import com.app.manage_money.model.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionDTO {
    private Integer id;
    private LabelDTO label;
    private AccountDTO account;
    private String name;
    private TransactionType transactionType;
    private String location;
    private BigDecimal amount;
    private String beneficiary;
    private String source;
    private boolean isRecurring;
    private LocalDate transactionDate;
}