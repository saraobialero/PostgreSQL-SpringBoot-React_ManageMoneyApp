package com.app.manage_money.service.functions;


import com.app.manage_money.model.dto.request.AddRecurringTransactionRequest;
import com.app.manage_money.model.dto.request.UpdateRecurringTransactionRequest;
import com.app.manage_money.model.dto.response.RecurringTransactionDTO;

import java.time.LocalDate;
import java.util.List;

public interface RecurringTransactionFunctions {
    // CREATE
    RecurringTransactionDTO addRecurringTransaction(AddRecurringTransactionRequest addRecurringTransactionRequest);

    // READ
    List<RecurringTransactionDTO> getDueTransactions(LocalDate date);
    List<RecurringTransactionDTO> getUpcomingTransactions(LocalDate date);
    List<RecurringTransactionDTO> getAllRecurringTransactions();
    RecurringTransactionDTO getRecurringTransactionById(Integer id);

    // UPDATE
    RecurringTransactionDTO updateRecurringTransaction(Integer id, UpdateRecurringTransactionRequest updateRecurringTransactionRequest);
    boolean toggleRecurringTransactionStatus(Integer id, boolean active);


}
