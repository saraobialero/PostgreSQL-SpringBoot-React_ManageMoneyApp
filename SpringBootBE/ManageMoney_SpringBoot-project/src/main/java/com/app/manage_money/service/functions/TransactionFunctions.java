package com.app.manage_money.service.functions;

import com.app.manage_money.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionFunctions {
    List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end);
    BigDecimal calculateTotalExpenses(Integer accountId, LocalDateTime start, LocalDateTime end);
    BigDecimal calculateTotalIncome(Integer accountId, LocalDateTime start, LocalDateTime end);
    void categorizeTransaction(Integer transactionId, Integer labelId);

}
