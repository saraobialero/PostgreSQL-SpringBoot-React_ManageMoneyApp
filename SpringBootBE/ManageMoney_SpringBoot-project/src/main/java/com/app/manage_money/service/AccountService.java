package com.app.manage_money.service;

import com.app.manage_money.model.Account;
import com.app.manage_money.model.dto.AccountDTO;
import com.app.manage_money.model.dto.TransactionDTO;
import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.State;
import com.app.manage_money.repository.AccountRepository;
import com.app.manage_money.service.functions.AccountFunctions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AccountService implements AccountFunctions {

 private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
 public AccountDTO addAccount(Account account) {
  return null;
 }

 @Override
 public AccountDTO getAccountById(Integer accountId) {
  return null;
 }

 @Override
 public Set<AccountDTO> getAccounts() {
  return Set.of();
 }

 @Override
 public BigDecimal calculateTotalBalance(Integer accountId) {
  return null;
 }

 @Override
 public Map<String, BigDecimal> getAccountAnalytics(Integer accountId, LocalDate startDate, LocalDate endDate) {
  return Map.of();
 }

 @Override
 public List<TransactionDTO> getAccountTransactions(Integer accountId, LocalDate startDate, LocalDate endDate) {
  return List.of();
 }

 @Override
 public boolean validateTransaction(Integer accountId, BigDecimal amount) {
  return false;
 }

 @Override
 public Map<CategoryType, BigDecimal> getExpensesByCategory(Integer accountId, LocalDate startDate, LocalDate endDate) {
  return Map.of();
 }

 @Override
 public Map<LabelType, BigDecimal> getExpensesByLabel(Integer accountId, LocalDate startDate, LocalDate endDate) {
  return Map.of();
 }

 @Override
 public BigDecimal updateBalance(Integer accountId, BigDecimal amount) {
  return null;
 }

 @Override
 public State updateAccountStatus(Integer accountId, State newState) {
  return null;
 }

 @Override
 public AccountDTO transferMoney(Integer sourceAccountId, Integer destinationAccountId, BigDecimal amount, String description) {
  return null;
 }

 @Override
 public boolean deleteAccountById(Integer accountId) {
  return false;
 }

 @Override
 public boolean deleteAll() {
  return false;
 }
}
