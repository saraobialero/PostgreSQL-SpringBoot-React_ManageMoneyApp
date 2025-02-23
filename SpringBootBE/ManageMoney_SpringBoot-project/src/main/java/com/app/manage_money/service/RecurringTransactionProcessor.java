package com.app.manage_money.service;

import com.app.manage_money.model.AccountRecurringTransaction;
import com.app.manage_money.model.RecurringTransaction;
import com.app.manage_money.model.Transaction;
import com.app.manage_money.model.enums.Frequency;
import com.app.manage_money.repository.RecurringTransactionRepository;
import com.app.manage_money.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecurringTransactionProcessor {
    private final RecurringTransactionRepository recurringTransactionRepository;
    private final TransactionRepository transactionRepository;

    @Scheduled(cron = "0 0 1 * * *") // Esegue ogni giorno all'1:00
    public void processRecurringTransactions() {
        LocalDate today = LocalDate.now();

        List<RecurringTransaction> dueTransactions = recurringTransactionRepository
                .findByIsActiveTrueAndNextOccurrenceGreaterThan(today);

        for (RecurringTransaction recurring : dueTransactions) {

            Transaction transaction = new Transaction();
            transaction.setLabel(recurring.getLabel());
            transaction.setAmount(recurring.getAmount());
            transaction.setTransactionType(recurring.getTransactionType());
            transaction.setBeneficiary(recurring.getBeneficiary());
            transaction.setSource(recurring.getSource());
            transaction.setRecurring(true);
            transaction.setTransactionDate(recurring.getNextOccurrence());

            // Collega all'account corretto
            AccountRecurringTransaction accountLink = recurring.getAccountRecurringTransactions()
                    .stream()
                    .findFirst()
                    .orElseThrow();
            transaction.setAccount(accountLink.getAccount());

            // Salva la transazione
            transactionRepository.save(transaction);

            // Aggiorna la recurring transaction
            recurring.setLastProcessedDate(recurring.getNextOccurrence());
            recurring.setNextOccurrence(calculateNextOccurrence(
                    recurring.getNextOccurrence(),
                    recurring.getFrequency()
            ));

            // Se ha superato end_date, disattiva
            if (recurring.getEndDate() != null &&
                    recurring.getNextOccurrence().isAfter(recurring.getEndDate())) {
                recurring.setActive(false);
            }

            recurringTransactionRepository.save(recurring);
        }
    }

    private LocalDate calculateNextOccurrence(LocalDate current, Frequency frequency) {
        return switch (frequency) {
            case DAILY -> current.plusDays(1);
            case WEEKLY -> current.plusWeeks(1);
            case MONTHLY -> current.plusMonths(1);
            case YEARLY -> current.plusYears(1);
        };
    }
}
