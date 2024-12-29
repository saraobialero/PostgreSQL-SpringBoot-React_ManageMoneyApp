package com.app.manage_money.service.functions;

import com.app.manage_money.model.Transaction;
import com.app.manage_money.model.dto.response.TransactionDTO;
import com.app.manage_money.model.dto.response.TransactionFilterDTO;
import com.app.manage_money.model.dto.response.TransferRequestDTO;
import com.app.manage_money.model.dto.response.TransferResultDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TransactionFunctions {
    // CREATE
    TransactionDTO addTransaction(Transaction transaction);
    TransferResultDTO transferMoney(TransferRequestDTO transferRequest);

    // READ
    TransactionDTO getTransactionById(Integer transactionId);
    List<TransactionDTO> getAllTransactions();
    List<TransactionDTO> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end);
    List<TransactionDTO> getTransactionsByAccount(Integer accountId);
    List<TransactionDTO> getTransactionsByLabel(Integer labelId);
    List<TransactionDTO> searchTransactions(String keyword);

    // READ - Analysis
    BigDecimal calculateTotalExpenses(Integer accountId, LocalDateTime start, LocalDateTime end);
    BigDecimal calculateTotalIncome(Integer accountId, LocalDateTime start, LocalDateTime end);
    BigDecimal calculateBalance(Integer accountId, LocalDateTime start, LocalDateTime end);
    Map<String, BigDecimal> getMonthlyTransactionSummary(Integer accountId, LocalDateTime date);

    // READ - filter
    List<TransactionDTO> filterTransactions(TransactionFilterDTO filterCriteria);
    List<TransactionDTO> getTransactionsByAmount(BigDecimal minAmount, BigDecimal maxAmount);
    List<TransactionDTO> getTransactionsByType(String type); // INCOME/EXPENSE

    // UPDATE
    boolean updateTransactionDetails(Integer transactionId, Transaction updatedTransaction);
    boolean categorizeTransaction(Integer transactionId, Integer labelId);
    boolean updateTransactionStatus(Integer transactionId, String status);

    // DELETE
    boolean deleteTransactionById(Integer transactionId);
    boolean deleteTransactionsByAccount(Integer accountId);
    boolean deleteTransactionsByDateRange(LocalDateTime start, LocalDateTime end);


}
