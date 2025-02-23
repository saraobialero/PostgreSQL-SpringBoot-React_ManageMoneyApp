package com.app.manage_money.service;

import com.app.manage_money.exception.LabelException;
import com.app.manage_money.exception.RecurringTransactionException;
import com.app.manage_money.exception.TransactionException;
import com.app.manage_money.model.AccountRecurringTransaction;
import com.app.manage_money.model.Label;
import com.app.manage_money.model.RecurringTransaction;
import com.app.manage_money.model.Transaction;
import com.app.manage_money.model.dto.request.AddRecurringTransactionRequest;
import com.app.manage_money.model.dto.request.AddTransactionRequest;
import com.app.manage_money.model.dto.request.UpdateRecurringTransactionRequest;
import com.app.manage_money.model.dto.response.ErrorResponse;
import com.app.manage_money.model.dto.response.RecurringTransactionDTO;
import com.app.manage_money.model.enums.ErrorCode;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.TransactionRole;
import com.app.manage_money.repository.AccountRecurringTransactionRepository;
import com.app.manage_money.repository.AccountRepository;
import com.app.manage_money.repository.LabelRepository;
import com.app.manage_money.repository.RecurringTransactionRepository;
import com.app.manage_money.service.functions.RecurringTransactionFunctions;
import com.app.manage_money.utils.DTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.app.manage_money.utils.DTOConverter.convertCollection;
import static com.app.manage_money.utils.DTOConverter.convertToRecurringTransactionDTO;

@RequiredArgsConstructor
@Service
public class RecurringTransactionService implements RecurringTransactionFunctions {

    private final RecurringTransactionRepository recurringTransactionRepository;
    private final LabelRepository labelRepository;
    private final AccountRepository accountRepository;
    private final AccountRecurringTransactionRepository accountRecurringTransactionRepository;



    @Override
    public RecurringTransactionDTO addRecurringTransaction(AddRecurringTransactionRequest request) {
        checkNotNullRequest(request);
        RecurringTransaction recurringTransaction = initializeRecurringTransactionFromRequest(request);

        Set<AccountRecurringTransaction> accountRecurringTransactions = new HashSet<>();

        // Configura l'account sorgente
        AccountRecurringTransaction sourceAccount = new AccountRecurringTransaction();
        sourceAccount.setAccount(accountRepository.getReferenceById(request.getSourceAccountId()));
        sourceAccount.setRecurringTransaction(recurringTransaction);
        sourceAccount.setTransactionRole(TransactionRole.SOURCE);
        accountRecurringTransactions.add(sourceAccount);

        // Configura l'account destinazione (se presente nella request)
        if (request.getDestinationAccountId() != null) {
            AccountRecurringTransaction destinationAccount = new AccountRecurringTransaction();
            destinationAccount.setAccount(accountRepository.getReferenceById(request.getDestinationAccountId()));
            destinationAccount.setRecurringTransaction(recurringTransaction);
            destinationAccount.setTransactionRole(TransactionRole.DESTINATION);
            accountRecurringTransactions.add(destinationAccount);
        }

        recurringTransaction.setAccountRecurringTransactions(accountRecurringTransactions);
        return convertToRecurringTransactionDTO(recurringTransactionRepository.save(recurringTransaction));
    }


    @Override
    public List<RecurringTransactionDTO> getDueTransactions(LocalDate date) {
        List<RecurringTransaction> recurringTransactions =
             recurringTransactionRepository.findByIsActiveTrueAndNextOccurrenceGreaterThan(date);
        recurringTransactionListIsEmpty(recurringTransactions);
        return convertCollection(recurringTransactions, DTOConverter::convertToRecurringTransactionDTO, ArrayList::new);
    }

    @Override
    public List<RecurringTransactionDTO> getUpcomingTransactions(LocalDate date) {
        List<RecurringTransaction> recurringTransactions =
                recurringTransactionRepository.findByIsActiveTrueAndNextOccurrenceBefore(date);
        recurringTransactionListIsEmpty(recurringTransactions);
        return convertCollection(recurringTransactions, DTOConverter::convertToRecurringTransactionDTO, ArrayList::new);

    }


    @Override
    public List<RecurringTransactionDTO> getAllRecurringTransactions() {
        List<RecurringTransaction> recurringTransactions = recurringTransactionRepository.findAll();
        recurringTransactionListIsEmpty(recurringTransactions);
        return convertCollection(recurringTransactions, DTOConverter::convertToRecurringTransactionDTO, ArrayList::new);

    }

    @Override
    public RecurringTransactionDTO getRecurringTransactionById(Integer id) {
        return convertToRecurringTransactionDTO(recurringTransactionExists(id));
    }

    @Override
    public RecurringTransactionDTO updateRecurringTransaction(Integer id, UpdateRecurringTransactionRequest updateRecurringTransactionRequest) {
        RecurringTransaction existingTransaction = recurringTransactionExists(id);
        RecurringTransaction updatedTransaction = updateExistingTransaction(existingTransaction, updateRecurringTransactionRequest);
        recurringTransactionRepository.save(updatedTransaction);
        return convertToRecurringTransactionDTO(updatedTransaction);
    }

    @Override
    public boolean toggleRecurringTransactionStatus(Integer id, boolean active) {
        RecurringTransaction recurringTransaction = recurringTransactionExists(id);
        recurringTransaction.setActive(!recurringTransaction.isActive());
        recurringTransactionRepository.save(recurringTransaction);
        return recurringTransaction.isActive();
    }

    // CUSTOM METHODS
    private void checkNotNullRequest(AddRecurringTransactionRequest request) {
        if (request == null){
            throw new RecurringTransactionException(
                    new ErrorResponse(
                            ErrorCode.NCT,
                            "RecurringTransaction with " + request.getSourceAccountId() + request.getFrequency() + " is null"));
        }
    }
    private RecurringTransaction initializeRecurringTransactionFromRequest(AddRecurringTransactionRequest request) {
       RecurringTransaction recurringTransaction = new RecurringTransaction();
       recurringTransaction.setTransactionType(request.getTransactionType());
       recurringTransaction.setBeneficiary(request.getBeneficiary());
       recurringTransaction.setAmount(request.getAmount());
       recurringTransaction.setActive(request.isActive());
       recurringTransaction.setLabel(getLabelByType(request.getLabelType()));
       recurringTransaction.setFrequency(request.getFrequency());
       recurringTransaction.setEndDate(request.getEndDate());
       recurringTransaction.setNextOccurrence(request.getNextOccurrence());
       recurringTransaction.setStartDate(request.getStartDate());
       recurringTransaction.setSource(request.getSource());

       return recurringTransaction;
    }
    private Label getLabelByType(LabelType labelType) {
        return labelRepository.findByCategoryLabelMapping_AllowedLabelType(labelType)
                .orElseThrow(() -> new LabelException(
                        new ErrorResponse(
                                ErrorCode.LNF,
                                "Label with type " + labelType + " not found")));
    }
    private void recurringTransactionListIsEmpty(List<RecurringTransaction> recurringTransactions) {
        if (recurringTransactions.isEmpty())
            throw new RecurringTransactionException(
                    new ErrorResponse(
                            ErrorCode.RTNC,
                            "No recurring transaction inside the list"));
    }
    private RecurringTransaction recurringTransactionExists(Integer id) {
        return recurringTransactionRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RecurringTransactionException(
                                new ErrorResponse(
                                        ErrorCode.RTNF,
                                        "RecurringTransaction with " + id + " not found")));

    }
    private RecurringTransaction updateExistingTransaction(RecurringTransaction existingTransaction, UpdateRecurringTransactionRequest updateRecurringTransactionRequest) {
        existingTransaction.setTransactionType(updateRecurringTransactionRequest.getTransactionType());
        existingTransaction.setBeneficiary(updateRecurringTransactionRequest.getBeneficiary());
        existingTransaction.setAmount(updateRecurringTransactionRequest.getAmount());
        existingTransaction.setActive(updateRecurringTransactionRequest.isActive());
        existingTransaction.setLabel(getLabelByType(updateRecurringTransactionRequest.getLabelType()));
        existingTransaction.setFrequency(updateRecurringTransactionRequest.getFrequency());
        existingTransaction.setEndDate(updateRecurringTransactionRequest.getEndDate());
        existingTransaction.setNextOccurrence(updateRecurringTransactionRequest.getNextOccurrence());
        existingTransaction.setStartDate(updateRecurringTransactionRequest.getStartDate());
        existingTransaction.setSource(updateRecurringTransactionRequest.getSource());
        return existingTransaction;
    }

}
