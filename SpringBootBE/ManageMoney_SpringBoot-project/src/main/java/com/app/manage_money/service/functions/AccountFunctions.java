package com.app.manage_money.service.functions;

import com.app.manage_money.model.Account;
import com.app.manage_money.model.dto.TransactionDTO;
import com.app.manage_money.model.dto.AccountDTO;

import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.State;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AccountFunctions {
    //CREATE
    AccountDTO addAccount (Account account);

    //READ
    AccountDTO getAccountById (Integer accountId);
    Set<AccountDTO> getAccounts ();

    BigDecimal calculateTotalBalance(Integer accountId);
    Map<String, BigDecimal> getAccountAnalytics(Integer accountId, LocalDate startDate, LocalDate endDate);

    List<TransactionDTO> getAccountTransactions(Integer accountId, LocalDate startDate, LocalDate endDate);
    boolean validateTransaction(Integer accountId, BigDecimal amount); // Verifica se c'è saldo sufficiente

    Map<CategoryType, BigDecimal> getExpensesByCategory(Integer accountId, LocalDate startDate, LocalDate endDate); //Move to label?

    Map<LabelType, BigDecimal> getExpensesByLabel(Integer accountId, LocalDate startDate, LocalDate endDate); // Move to label?


    //UPDATE
    BigDecimal updateBalance(Integer accountId, BigDecimal amount);
    State updateAccountStatus(Integer accountId, State newState);

    AccountDTO transferMoney(Integer sourceAccountId, Integer destinationAccountId, BigDecimal amount, String description); //Move to Transaction???

    //DELETE
    boolean deleteAccountById (Integer accountId);
    boolean deleteAll();




}
