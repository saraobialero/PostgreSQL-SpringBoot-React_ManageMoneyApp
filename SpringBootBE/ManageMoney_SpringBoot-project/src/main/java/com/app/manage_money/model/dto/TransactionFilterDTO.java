package com.app.manage_money.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransactionFilterDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> categories;
    private List<String> labels;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String searchText;
    private List<Integer> accountIds;
    private String transactionType; // INCOME/EXPENSE
}
