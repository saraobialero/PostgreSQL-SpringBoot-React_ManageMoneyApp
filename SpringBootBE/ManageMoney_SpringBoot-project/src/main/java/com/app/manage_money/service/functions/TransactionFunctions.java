package com.app.manage_money.service.functions;

import com.app.manage_money.model.dto.request.AddTransactionRequest;
import com.app.manage_money.model.dto.request.TransactionUpdateRequest;
import com.app.manage_money.model.dto.response.*;
import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionFunctions {
    // CREATE
    TransactionDTO addTransaction(AddTransactionRequest request);

    // READ
    TransactionDTO getTransactionById(Integer transactionId);
    List<TransactionDTO> getAllTransactions();
    List<TransactionDTO> getTransactionsByDateRange(LocalDate start, LocalDate end);
    List<TransactionDTO> getAllTransactionsByAccount(Integer accountId);
    List<TransactionDTO> getTransactionsByLabel(LabelType labelType);
    List<TransactionDTO> getTransactionsByCategory(CategoryType categoryType);

    // READ - Analysis
    BigDecimal calculateTotalExpenses(LocalDate start, LocalDate end);
    BigDecimal calculateTotalIncomes( LocalDate start, LocalDate end);
    TransactionMonthlyAnalyticsDTO getMonthlyTransactionSummary(Integer accountId, LocalDate date);

    // READ - filter
    List<TransactionDTO> getTransactionsByAmount(BigDecimal minAmount, BigDecimal maxAmount);
    List<TransactionDTO> getTransactionsByType(TransactionType transactionType); // INCOME/EXPENSE

    // UPDATE
    TransactionDTO updateTransactionDetails(Integer transactionId, TransactionUpdateRequest updateRequest);
    LabelType categorizeTransaction(Integer transactionId, LabelType labelType);
    TransactionType updateTransactionType(Integer transactionId, TransactionType transactionType);

    // DELETE
    boolean deleteTransactionById(Integer transactionId);
    boolean deleteTransactionsByAccount(Integer accountId);
    boolean deleteTransactionsByDateRangeAndAccount(Integer accountId, LocalDate start, LocalDate end);
    boolean deleteAll();


}
