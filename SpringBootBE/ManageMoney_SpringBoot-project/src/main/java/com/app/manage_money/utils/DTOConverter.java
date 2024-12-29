package com.app.manage_money.utils;


import com.app.manage_money.model.*;
import com.app.manage_money.model.dto.response.*;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DTOConverter {

    public static <T, R, C extends Collection<R>> C convertCollection(Collection<T> source,
                                                                      Function<T, R> converter,
                                                                      Supplier<C> collectionFactory) {
        return source.stream()
                .map(converter)
                .collect(Collectors.toCollection(collectionFactory));
    }

    public static AccountDTO convertToAccountDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountType(account.getAccountType())
                .state(account.getState())
                .balance(account.getBalance())
                .build();
    }

    public static TransactionDTO convertToTransactionDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .name(transaction.getName())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .location(transaction.getLocation())
                .beneficiary(transaction.getBeneficiary())
                .source(transaction.getSource())
                .isRecurring(transaction.isRecurring())
                .transactionDate(transaction.getTransactionDate())
                .label(convertToLabelDTO(transaction.getLabel()))
                .build();
    }

    public static LabelDTO convertToLabelDTO(Label label) {
        return LabelDTO.builder()
                .id(label.getId())
                .categoryLabelMapping(convertToCategoryLabelMappingDTO(label.getCategoryLabelMapping()))
                .transactionType(label.getTransactionType())
                .isActive(label.isActive())
                .build();
    }

    public static CategoryLabelMappingDTO convertToCategoryLabelMappingDTO(CategoryLabelMapping mapping) {
        return CategoryLabelMappingDTO.builder()
                .id(mapping.getId())
                .categoryType(mapping.getCategoryType())
                .allowedLabelType(mapping.getAllowedLabelType())
                .transactionType(mapping.getTransactionType())
                .build();
    }

    public static RecurringTransactionDTO convertToRecurringTransactionDTO(RecurringTransaction recurringTransaction) {
        return RecurringTransactionDTO.builder()
                .id(recurringTransaction.getId())
                .transactionType(recurringTransaction.getTransactionType())
                .amount(recurringTransaction.getAmount())
                .frequency(recurringTransaction.getFrequency())
                .startDate(recurringTransaction.getStartDate())
                .endDate(recurringTransaction.getEndDate())
                .nextOccurrence(recurringTransaction.getNextOccurrence())
                .beneficiary(recurringTransaction.getBeneficiary())
                .source(recurringTransaction.getSource())
                .lastProcessedDate(recurringTransaction.getLastProcessedDate())
                .isActive(recurringTransaction.isActive())
                .label(convertToLabelDTO(recurringTransaction.getLabel()))
                .build();
    }

    public static SavingPlanDTO convertToSavingPlanDTO(SavingPlan savingPlan) {
        return SavingPlanDTO.builder()
                .id(savingPlan.getId())
                .targetAmount(savingPlan.getTargetAmount())
                .currentAmount(savingPlan.getCurrentAmount())
                .startDate(savingPlan.getStartDate())
                .targetDate(savingPlan.getTargetDate())
                .account(convertToAccountDTO(savingPlan.getAccount()))
                .build();
    }

    public static AccountRecurringTransactionDTO convertToAccountRecurringTransactionDTO(AccountRecurringTransaction art) {
        return AccountRecurringTransactionDTO.builder()
                .id(art.getId())
                .account(convertToAccountDTO(art.getAccount()))
                .recurringTransaction(convertToRecurringTransactionDTO(art.getRecurringTransaction()))
                .transactionRole(art.getTransactionRole())
                .build();
    }
}
