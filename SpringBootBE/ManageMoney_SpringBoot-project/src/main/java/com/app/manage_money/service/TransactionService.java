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
import com.app.manage_money.model.enums.CategoryType;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


import static com.app.manage_money.utils.DTOConverter.*;

@RequiredArgsConstructor
@Service
public class TransactionService implements TransactionFunctions {
    private final TransactionRepository transactionRepository;
    private final LabelRepository labelRepository;
    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public TransactionDTO addTransaction(AddTransactionRequest request) {
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
    public List<TransactionDTO> getTransactionsByDateRange(LocalDate start, LocalDate end) {
        List<Transaction> transactions = transactionRepository
                                        .findByTransactionDateBetween(start, end);
        transactionListIsEmpty(transactions);
        return convertCollection(transactions, DTOConverter::convertToTransactionDTO, ArrayList::new);
    }

    @Override
    public List<TransactionDTO> getAllTransactionsByAccount(Integer accountId) {
        getAccountById(accountId);
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        transactionListIsEmpty(transactions);
        return convertCollection(transactions, DTOConverter::convertToTransactionDTO, ArrayList::new);
    }

    @Override
    public List<TransactionDTO> getTransactionsByLabel(LabelType labelType) {
        List<Transaction> transactions = transactionRepository
                                        .findByLabel_CategoryLabelMapping_AllowedLabelType(labelType);
        transactionListIsEmptyBy(transactions, labelType);
        return convertCollection(transactions, DTOConverter::convertToTransactionDTO, ArrayList::new);
    }

    @Override
    public List<TransactionDTO> getTransactionsByCategory(CategoryType categoryType) {
        List<Transaction> transactions = transactionRepository
                                        .findByLabel_CategoryLabelMapping_CategoryType(categoryType);
        transactionListIsEmptyBy(transactions, categoryType);
        return convertCollection(transactions, DTOConverter::convertToTransactionDTO, ArrayList::new);
    }


    @Override
    public BigDecimal calculateTotalExpenses(LocalDate start, LocalDate end) {
        List<Transaction> transactions = transactionRepository
                .findByTransactionDateBetweenAndTransactionType(
                        start,
                        end,
                        TransactionType.EXPENSE);

        transactionListIsEmptyBy(transactions,
                String.format("date range %s - %s", start, end));

        return getAmount(transactions);
    }

    @Override
    public BigDecimal calculateTotalIncomes(LocalDate start, LocalDate end) {
        List<Transaction> transactions = transactionRepository
                .findByTransactionDateBetweenAndTransactionType(
                        start,
                        end,
                        TransactionType.INCOME);

        transactionListIsEmptyBy(transactions,
                String.format("date range %s - %s", start, end));

        return getAmount(transactions);
    }


    @Override
    public TransactionMonthlyAnalyticsDTO getMonthlyTransactionSummary(Integer accountId, LocalDate date) {
        Account account = getAccountById(accountId);

        // Calcola date di inizio e fine mese
        YearMonth yearMonth = YearMonth.from(date);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // Ottieni tutte le transazioni del mese
        List<Transaction> monthlyTransactions = transactionRepository
                .findByAccountIdAndTransactionDateBetween(accountId, startDate, endDate);

        // Verifica se ci sono transazioni
        transactionListIsEmptyBy(monthlyTransactions,
                String.format("month %s for account %d", yearMonth, accountId));

        // Ottieni transazioni del mese precedente per confronto
        LocalDate previousMonthStart = startDate.minusMonths(1);
        LocalDate previousMonthEnd = endDate.minusMonths(1);
        List<Transaction> previousMonthTransactions = transactionRepository
                .findByAccountIdAndTransactionDateBetween(accountId, previousMonthStart, previousMonthEnd);

        // Usa il DTOConverter per costruire l'analytics DTO
        return DTOConverter.buildTransactionAnalyticsDTO(
                monthlyTransactions,
                previousMonthTransactions,
                yearMonth,
                startDate,
                endDate
        );
    }

    @Override
    public List<TransactionDTO> getTransactionsByAmount(BigDecimal minAmount, BigDecimal maxAmount) {
        List<Transaction> transactions = transactionRepository
                                        .findByAmountBetween(minAmount, maxAmount);

        transactionListIsEmptyBy(transactions,
                String.format("amount range %s - %s", minAmount, maxAmount));

        return convertCollection(transactions, DTOConverter::convertToTransactionDTO, ArrayList::new);
    }

    @Override
    public List<TransactionDTO> getTransactionsByType(TransactionType transactionType) {
        List<Transaction> transactions = transactionRepository.findByTransactionType(transactionType);
        transactionListIsEmptyBy(transactions, transactionType);
        return convertCollection(transactions, DTOConverter::convertToTransactionDTO, ArrayList::new);
    }

    @Override
    public TransactionDTO updateTransactionDetails(Integer transactionId, TransactionUpdateRequest updateRequest) {
        Transaction transaction = transactionExists(transactionId);
        updateTransactionFromRequest(transaction, updateRequest);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToTransactionDTO(updatedTransaction);
    }

    @Override
    @Transactional
    public LabelType categorizeTransaction(Integer transactionId, LabelType labelType) {
        Transaction transaction = transactionExists(transactionId);
        Label newLabel = getLabelByType(labelType);
        transaction.setLabel(newLabel);
        transactionRepository.save(transaction);
        return labelType;
    }

    @Override
    @Transactional
    public TransactionType updateTransactionType(Integer transactionId, TransactionType transactionType) {
        Transaction transaction = transactionExists(transactionId);
        transaction.setTransactionType(transactionType);
        transactionRepository.save(transaction);
        return transactionType;
    }


    @Override
    @Transactional
    public boolean deleteTransactionById(Integer transactionId) {
        Transaction transaction = transactionExists(transactionId);
        transactionRepository.delete(transaction);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteTransactionsByAccount(Integer accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        transactionListIsEmptyBy(transactions, accountId);
        transactionRepository.deleteAll(transactions);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteTransactionsByDateRangeAndAccount(Integer accountId, LocalDate start, LocalDate end) {
        List<Transaction> transactions = transactionRepository
                                        .findByAccountIdAndTransactionDateBetween(accountId, start, end);
        transactionListIsEmptyBy(transactions, String.format("between dates %s - %s", start, end));
        return true;
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        List<Transaction> transactions = transactionListExists();
        transactionListIsEmpty(transactions);
        transactionRepository.deleteAll();
        return true;
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
        return labelRepository.findByCategoryLabelMapping_AllowedLabelType(labelType)
                .orElseThrow(() -> new LabelException(
                        new ErrorResponse(
                                ErrorCode.LNF,
                                "Label with type " + labelType + " not found")));
    }
    private <T> void transactionListIsEmptyBy(List<Transaction> transactions, T filter) {
        if (transactions.isEmpty()) {
            throw new TransactionException(
                    new ErrorResponse(
                            ErrorCode.NCTS,
                            "No transactions found for: " + filter));
        }
    }
    private Account getAccountById (Integer accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException(
                        new ErrorResponse(
                                ErrorCode.ANF,
                                "Account with id " + accountId + " not found")));
    }
    private BigDecimal getAmount(List<Transaction> transactions) {
        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    private void updateTransactionFromRequest(Transaction transaction, TransactionUpdateRequest request) {
        transaction.setLabel(getLabelByType(request.getLabelType()));
        transaction.setName(request.getName());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setLocation(request.getLocation());
        transaction.setAmount(request.getAmount());
        transaction.setBeneficiary(request.getBeneficiary());
        transaction.setSource(request.getSource());
        transaction.setRecurring(request.getIsRecurring());
        transaction.setTransactionDate(request.getTransactionDate());
    }
    private BigDecimal calculateTotalIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    private BigDecimal calculateTotalExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    private BigDecimal calculateNetBalance(List<Transaction> transactions) {
        return calculateTotalIncome(transactions).subtract(calculateTotalExpenses(transactions));
    }

}
