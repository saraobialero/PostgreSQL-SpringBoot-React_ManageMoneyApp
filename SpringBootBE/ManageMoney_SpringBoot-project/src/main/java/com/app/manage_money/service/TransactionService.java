package com.app.manage_money.service;

import com.app.manage_money.exception.AccountException;
import com.app.manage_money.exception.LabelException;
import com.app.manage_money.exception.TransactionException;
import com.app.manage_money.model.Account;
import com.app.manage_money.model.Label;
import com.app.manage_money.model.Transaction;
import com.app.manage_money.model.dto.request.AddTransactionRequest;
import com.app.manage_money.model.dto.request.TransactionUpdateRequest;
import com.app.manage_money.model.dto.response.ErrorResponse;
import com.app.manage_money.model.dto.response.TransactionDTO;
import com.app.manage_money.model.dto.response.TransactionMonthlyAnalyticsDTO;
import com.app.manage_money.model.enums.ErrorCode;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.TransactionType;
import com.app.manage_money.repository.AccountRepository;
import com.app.manage_money.repository.LabelRepository;
import com.app.manage_money.repository.TransactionRepository;
import com.app.manage_money.service.functions.TransactionFunctions;
import com.app.manage_money.utils.DTOConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.app.manage_money.utils.DTOConverter.convertCollection;
import static com.app.manage_money.utils.DTOConverter.convertToTransactionDTO;

@RequiredArgsConstructor
@Service
public class TransactionService implements TransactionFunctions {
    private final TransactionRepository transactionRepository;
    private final LabelRepository labelRepository;
    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public TransactionDTO addTransaction(AddTransactionRequest request, Integer accountId) {
        checkNotNullRequest(request);
        Transaction transaction = initializeTransactionFromRequest(request);
        transactionRepository.save(transaction);
        return convertToTransactionDTO(transaction);
    }

    @Override
    public TransactionDTO getTransactionById(Integer transactionId) {
        Transaction transaction = transactionExists(transactionId);
        return convertToTransactionDTO(transaction);
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionListExists();
        transactionListIsEmpty(transactions);
        return convertCollection(transactions, DTOConverter::convertToTransactionDTO, ArrayList::new);
    }

    @Override
    public List<TransactionDTO> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return List.of();
    }

    @Override
    public List<TransactionDTO> getTransactionsByAccount(Integer accountId) {
        return List.of();
    }

    @Override
    public List<TransactionDTO> getTransactionsByLabel(Integer labelId) {
        return List.of();
    }

    @Override
    public List<TransactionDTO> searchTransactions(String keyword) {
        return List.of();
    }

    @Override
    public List<TransactionDTO> getTransactionsByRecurringId(Integer recurringTransactionId) {
        return List.of();
    }

    @Override
    public BigDecimal calculateTotalExpenses(Integer accountId, LocalDateTime start, LocalDateTime end) {
        return null;
    }

    @Override
    public BigDecimal calculateTotalIncome(Integer accountId, LocalDateTime start, LocalDateTime end) {
        return null;
    }

    @Override
    public BigDecimal calculateBalance(Integer accountId, LocalDateTime start, LocalDateTime end) {
        return null;
    }

    @Override
    public TransactionMonthlyAnalyticsDTO getMonthlyTransactionSummary(Integer accountId, LocalDateTime date) {
        return null;
    }

    @Override
    public List<TransactionDTO> getTransactionsByAmount(BigDecimal minAmount, BigDecimal maxAmount) {
        return List.of();
    }

    @Override
    public List<TransactionDTO> getTransactionsByType(String type) {
        return List.of();
    }

    @Override
    public TransactionDTO updateTransactionDetails(Integer transactionId, TransactionUpdateRequest updateRequest) {
        return null;
    }

    @Override
    public LabelType categorizeTransaction(Integer transactionId, LabelType labelType) {
        return null;
    }

    @Override
    public TransactionType updateTransactionType(Integer transactionId, TransactionType transactionType) {
        return null;
    }

    @Override
    public boolean linkToRecurringTransaction(Integer transactionId, Integer recurringTransactionId) {
        return false;
    }

    @Override
    public boolean unlinkFromRecurringTransaction(Integer transactionId) {
        return false;
    }

    @Override
    public boolean deleteTransactionById(Integer transactionId) {
        return false;
    }

    @Override
    public boolean deleteTransactionsByAccount(Integer accountId) {
        return false;
    }

    @Override
    public boolean deleteTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return false;
    }

    @Override
    public boolean deleteAll() {
        return false;
    }

    // CUSTOM METHODS
    private Transaction transactionExists (Integer transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionException(
                        new ErrorResponse(
                                ErrorCode.TNF,
                                "Transaction with id " + transactionId + " not found")));
    }
    private List<Transaction> transactionListExists () {
        return transactionRepository.findAll();
    }
    private void transactionListIsEmpty(List<Transaction> transactions) {
        if(transactions.isEmpty()) {
            throw new TransactionException(
                    new ErrorResponse(
                            ErrorCode.NCTS,
                            "No content inside the list"));
        }
    }
    private void checkNotNullRequest(AddTransactionRequest request) {
        if (request == null){
            throw new TransactionException(
                    new ErrorResponse(
                            ErrorCode.NCT,
                            "Transaction with " + request.getName() + " is null"));
        }
    }
    private Transaction initializeTransactionFromRequest(AddTransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.setLabel(getLabelByType(request.getLabelType()));
        transaction.setAccount(getAccountById(request.getAccountId()));
        transaction.setName(request.getName());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setLocation(request.getLocation());
        transaction.setAmount(request.getAmount());
        transaction.setBeneficiary(request.getBeneficiary());
        transaction.setSource(request.getSource());
        transaction.setRecurring(request.isRecurring());
        transaction.setTransactionDate(request.getTransactionDate());
        return transaction;
    }
    private Label getLabelByType(LabelType labelType) {
        return labelRepository.findByLabelType(labelType)
                .orElseThrow(() -> new LabelException(
                        new ErrorResponse(
                                ErrorCode.LNF,
                                "Label with id " + labelType + " not found")));
    }
    private Account getAccountById (Integer accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException(
                        new ErrorResponse(
                                ErrorCode.ANF,
                                "Account with id " + accountId + " not found")));
    }
}
