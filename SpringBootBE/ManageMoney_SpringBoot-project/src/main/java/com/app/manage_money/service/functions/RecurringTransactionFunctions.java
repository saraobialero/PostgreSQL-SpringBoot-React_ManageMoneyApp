package com.app.manage_money.service.functions;

import com.app.manage_money.model.RecurringTransaction;

import java.util.List;

public interface RecurringTransactionFunctions {
    void processRecurringTransactions();
    List<RecurringTransaction> getDueTransactions();
    void updateNextOccurrence(Integer recurringTransactionId);
    void pauseRecurringTransaction(Integer recurringTransactionId);

}
