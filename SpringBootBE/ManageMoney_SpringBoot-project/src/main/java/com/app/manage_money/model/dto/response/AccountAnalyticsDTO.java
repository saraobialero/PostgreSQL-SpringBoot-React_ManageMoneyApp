package com.app.manage_money.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class AccountAnalyticsDTO {
    private BigDecimal currentBalance;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal cashFlow;
    private LocalDate startDate;
    private LocalDate endDate;
}
