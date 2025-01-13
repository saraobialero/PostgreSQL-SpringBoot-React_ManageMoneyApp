package com.app.manage_money.model.dto.response;

import com.app.manage_money.model.enums.CategoryType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Data
@Builder
public class TransactionMonthlyAnalyticsDTO {
    // Totali principali
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance; // income - expenses

    // Metriche su transazioni
    private Integer totalTransactions;
    private BigDecimal averageTransactionAmount;
    private BigDecimal largestExpense;
    private BigDecimal largestIncome;

    // Analisi per categoria
    private Map<CategoryType, BigDecimal> expensesByCategory;
    private Map<CategoryType, BigDecimal> incomeByCategory;
    private CategoryType topExpenseCategory;
    private CategoryType topIncomeCategory;

    // Confronto con il mese precedente
    private BigDecimal expensesChangeSinceLastMonth; // in percentuale
    private BigDecimal incomeChangeSinceLastMonth;   // in percentuale

    // Metriche di budget
    private BigDecimal monthlyBudget;
    private BigDecimal budgetRemaining;
    private Double budgetUsagePercentage;

    // Date di riferimento
    private YearMonth period;
    private LocalDate startDate;
    private LocalDate endDate;

    // Statistiche giornaliere
    private Map<LocalDate, BigDecimal> dailyExpenses;
    private Map<LocalDate, BigDecimal> dailyIncome;
    private LocalDate highestExpenseDay;
    private LocalDate highestIncomeDay;

    // Metriche ricorrenti
    private BigDecimal recurringExpensesTotal;
    private BigDecimal recurringIncomesTotal;
    private Integer recurringTransactionsCount;

    // KPIs (Key Performance Indicators)
    private Double savingsRate;         // (income - expenses) / income
    private Double expenseToIncomeRatio; // expenses / income
}
