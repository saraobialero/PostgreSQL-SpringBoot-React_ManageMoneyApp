package com.app.manage_money.service.functions;

import com.app.manage_money.model.Account;
import com.app.manage_money.model.dto.request.AddAccountRequest;
import com.app.manage_money.model.dto.request.TransferMoneyRequest;
import com.app.manage_money.model.dto.response.AccountAnalyticsDTO;
import com.app.manage_money.model.dto.response.TransactionDTO;
import com.app.manage_money.model.dto.response.AccountDTO;

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
    AccountDTO addAccount (AddAccountRequest request);

    //READ
    AccountDTO getAccountById (Integer accountId);
    Set<AccountDTO> getAccounts ();

    BigDecimal calculateTotalBalance();
    AccountAnalyticsDTO getAccountAnalytics(Integer accountId, LocalDate startDate, LocalDate endDate);
    List<TransactionDTO> getAccountTransactions(Integer accountId, LocalDate startDate, LocalDate endDate);
    List<TransactionDTO> getExpensesByAccountAndLabelType( Integer accountId, LabelType labelType, LocalDate startDate, LocalDate endDate);
    List<TransactionDTO> getExpensesByAccountAndCategory(Integer accountId, CategoryType category, LocalDate startDate, LocalDate endDate);

    //UPDATE
    BigDecimal updateBalance(Integer accountId, BigDecimal amount);
    State updateAccountStatus(Integer accountId, State newState);

    Set<AccountDTO> transferMoney(Integer sourceAccountId, Integer destinationAccountId, TransferMoneyRequest request);

    //DELETE
    boolean deleteAccountById (Integer accountId);
    boolean deleteAll();




}
