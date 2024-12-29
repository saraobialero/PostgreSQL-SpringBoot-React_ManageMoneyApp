package com.app.manage_money.model.dto.response;

import com.app.manage_money.model.enums.TransactionRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRecurringTransactionDTO {
    private Integer id;
    private AccountDTO account;
    private RecurringTransactionDTO recurringTransaction;
    private TransactionRole transactionRole;
}