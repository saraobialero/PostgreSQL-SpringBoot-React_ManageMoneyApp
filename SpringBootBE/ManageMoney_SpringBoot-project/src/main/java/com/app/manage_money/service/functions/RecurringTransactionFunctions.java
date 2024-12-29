package com.app.manage_money.service.functions;

import com.app.manage_money.model.RecurringTransaction;
import com.app.manage_money.model.dto.response.RecurringTransactionDTO;
import com.app.manage_money.model.enums.Frequency;

import java.time.LocalDate;
import java.util.List;

public interface RecurringTransactionFunctions {
    // CREATE
    RecurringTransactionDTO addRecurringTransaction(RecurringTransaction transaction, Frequency frequency);

    // READ
    List<RecurringTransactionDTO> getDueTransactions();
    List<RecurringTransactionDTO> getUpcomingTransactions(LocalDate startDate, LocalDate endDate);
    RecurringTransactionDTO getRecurringTransactionById(Integer id);

    // UPDATE
    boolean updateNextOccurrence(Integer recurringTransactionId);
    boolean updateRecurringTransaction(Integer id, RecurringTransaction updatedTransaction);
    boolean toggleRecurringTransactionStatus(Integer id, boolean active);

    // Process
    void processRecurringTransactions();
    List<RecurringTransactionDTO> getFailedRecurringTransactions();

    // DELETE
    boolean deleteRecurringTransaction(Integer id);

}
