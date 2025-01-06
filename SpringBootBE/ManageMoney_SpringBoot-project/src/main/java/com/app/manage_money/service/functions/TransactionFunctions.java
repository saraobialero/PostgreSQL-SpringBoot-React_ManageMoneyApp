package com.app.manage_money.service.functions;

import com.app.manage_money.model.dto.request.AddTransactionRequest;
import com.app.manage_money.model.dto.request.TransactionUpdateRequest;
import com.app.manage_money.model.dto.response.*;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionFunctions {
    // CREATE
    TransactionDTO addTransaction(AddTransactionRequest request, Integer accountId);

    // READ
    TransactionDTO getTransactionById(Integer transactionId);
    List<TransactionDTO> getAllTransactions();
    List<TransactionDTO> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end);
    List<TransactionDTO> getTransactionsByAccount(Integer accountId);
    List<TransactionDTO> getTransactionsByLabel(Integer labelId);
    List<TransactionDTO> searchTransactions(String keyword);
    List<TransactionDTO> getTransactionsByRecurringId(Integer recurringTransactionId);

    // READ - Analysis
    BigDecimal calculateTotalExpenses(Integer accountId, LocalDateTime start, LocalDateTime end);
    BigDecimal calculateTotalIncome(Integer accountId, LocalDateTime start, LocalDateTime end);
    BigDecimal calculateBalance(Integer accountId, LocalDateTime start, LocalDateTime end);
    TransactionMonthlyAnalyticsDTO getMonthlyTransactionSummary(Integer accountId, LocalDateTime date);

    // READ - filter
    List<TransactionDTO> getTransactionsByAmount(BigDecimal minAmount, BigDecimal maxAmount);
    List<TransactionDTO> getTransactionsByType(String type); // INCOME/EXPENSE

    // UPDATE
    TransactionDTO updateTransactionDetails(Integer transactionId, TransactionUpdateRequest updateRequest);
    LabelType categorizeTransaction(Integer transactionId, LabelType labelType);
    TransactionType updateTransactionType(Integer transactionId, TransactionType transactionType);
    boolean linkToRecurringTransaction(Integer transactionId, Integer recurringTransactionId);
    boolean unlinkFromRecurringTransaction(Integer transactionId);


    // DELETE
    boolean deleteTransactionById(Integer transactionId);
    boolean deleteTransactionsByAccount(Integer accountId);
    boolean deleteTransactionsByDateRange(LocalDateTime start, LocalDateTime end);
    boolean deleteAll();


}
