package com.app.manage_money.service;

import com.app.manage_money.exception.AccountException;
import com.app.manage_money.exception.LabelException;
import com.app.manage_money.model.Account;
import com.app.manage_money.model.Label;
import com.app.manage_money.model.Transaction;
import com.app.manage_money.model.dto.request.AddAccountRequest;
import com.app.manage_money.model.dto.request.TransferMoneyRequest;
import com.app.manage_money.model.dto.response.AccountDTO;
import com.app.manage_money.model.dto.response.ErrorResponse;
import com.app.manage_money.model.dto.response.TransactionDTO;
import com.app.manage_money.model.enums.*;
import com.app.manage_money.repository.AccountRepository;
import com.app.manage_money.repository.LabelRepository;
import com.app.manage_money.repository.TransactionRepository;
import com.app.manage_money.service.functions.AccountFunctions;
import com.app.manage_money.utils.BigDecimalUtils;
import com.app.manage_money.utils.DTOConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.app.manage_money.utils.DTOConverter.convertCollection;
import static com.app.manage_money.utils.DTOConverter.convertToAccountDTO;

@RequiredArgsConstructor
@Service
public class AccountService implements AccountFunctions {
 private final AccountRepository accountRepository;
 private final TransactionRepository transactionRepository;
 private final LabelRepository labelRepository;

 @Transactional
 @Override
 public AccountDTO addAccount(AddAccountRequest request) {
  checkNotNullRequest(request);
  Account account = initializeAccountFromRequest(request);
  accountRepository.save(account);
  return convertToAccountDTO(account);
 }

 @Override
 public AccountDTO getAccountById(Integer accountId) {
  Account account = accountExists(accountId);
  return convertToAccountDTO(account);
 }


 @Override
 public Set<AccountDTO> getAccounts() {
  Set<Account> accounts = accountListExists();
  accountListIsEmpty(accounts);
  return convertCollection(accounts, DTOConverter::convertToAccountDTO, HashSet::new);
 }

 @Override
 public BigDecimal calculateTotalBalance() {
  Set<Account> accounts = accountListExists();
  accountListIsEmpty(accounts);

  return accounts.stream()
          .map(Account::getBalance)
          .filter(balance -> balance != null)
          .reduce(BigDecimal.ZERO, BigDecimal::add);
 }

 @Override
 public Map<String, BigDecimal> getAccountAnalytics(Integer accountId, LocalDate startDate, LocalDate endDate) {
  Account account = accountExists(accountId);

  List<Transaction> transactions = transactionRepository.findByAccountIdAndTransactionDateBetween(
          accountId, startDate, endDate);

  BigDecimal totalIncome = transactions.stream()
          .filter(t -> t.getTransactionType() == TransactionType.INCOME)
          .map(Transaction::getAmount)
          .reduce(BigDecimal.ZERO, BigDecimal::add);

  BigDecimal totalExpenses = transactions.stream()
          .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
          .map(Transaction::getAmount)
          .reduce(BigDecimal.ZERO, BigDecimal::add);

  BigDecimal balance = account.getBalance();
  BigDecimal cashFlow = totalIncome.subtract(totalExpenses);

  return Map.of(
          "totalIncome", totalIncome,
          "totalExpenses", totalExpenses,
          "balance", balance,
          "cashFlow", cashFlow
  );
 }

 @Override
 public List<TransactionDTO> getAccountTransactions(Integer accountId, LocalDate startDate, LocalDate endDate) {
  accountExists(accountId);

  List<Transaction> transactions = transactionRepository.findByAccountIdAndTransactionDateBetween(
          accountId, startDate, endDate);

  return transactions.stream()
          .map(DTOConverter::convertToTransactionDTO)
          .collect(Collectors.toList());
 }

 @Override
 public Map<CategoryType, BigDecimal> getExpensesByCategory(Integer accountId, LocalDate startDate, LocalDate endDate) {
  accountExists(accountId);

  List<Transaction> expenses = transactionRepository.findByAccountIdAndTransactionTypeAndTransactionDateBetween(
          accountId, TransactionType.EXPENSE, startDate, endDate);

  return expenses.stream()
          .filter(t -> t.getLabel() != null)
          .collect(Collectors.groupingBy(
                  t -> t.getLabel().getCategoryLabelMapping().getCategoryType(),
                  Collectors.mapping(
                          Transaction::getAmount,
                          Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                  )
          ));
 }

 @Override
 public Map<LabelType, BigDecimal> getExpensesByLabel(Integer accountId, LocalDate startDate, LocalDate endDate) {
  accountExists(accountId);

  List<Transaction> expenses = transactionRepository.findByAccountIdAndTransactionTypeAndTransactionDateBetween(
          accountId, TransactionType.EXPENSE, startDate, endDate);

  return expenses.stream()
          .filter(t -> t.getLabel() != null)
          .collect(Collectors.groupingBy(
                  t -> t.getLabel().getCategoryLabelMapping().getAllowedLabelType(),
                  Collectors.mapping(
                          Transaction::getAmount,
                          Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                  )
          ));
 }

 @Transactional
 @Override
 public BigDecimal updateBalance(Integer accountId, BigDecimal amount) {
  Account account = accountExists(accountId);
  checkAccountState(account);
  account.setBalance(amount);
  accountRepository.save(account);
  return account.getBalance();
 }

 @Transactional
 @Override
 public State updateAccountStatus(Integer accountId, State newState) {
  Account account = accountExists(accountId);
  account.setState(newState);
  accountRepository.save(account);
  return account.getState();
 }

 @Transactional
 @Override
 public Set<AccountDTO> transferMoney(Integer sourceAccountId, Integer destinationAccountId, TransferMoneyRequest request) {
  Account sourceAccount = accountExists(sourceAccountId);
  Account destinationAccount = accountExists(destinationAccountId);

  validateTransfer(sourceAccount, destinationAccount);
  checkMajorBalance(sourceAccount, request);
  executeTransfer(sourceAccount, destinationAccount, request);

  accountRepository.save(destinationAccount);
  accountRepository.save(sourceAccount);

  Set<Account> accounts = new HashSet<>();
  accounts.add(sourceAccount);
  accounts.add(destinationAccount);
  return convertCollection(accounts, DTOConverter::convertToAccountDTO, HashSet::new);
 }

 @Transactional
 @Override
 public boolean deleteAccountById(Integer accountId) {
  Account account = accountExists(accountId);
  accountRepository.delete(account);
  return true;
 }

 @Transactional
 @Override
 public boolean deleteAll() {
  Set<Account> accounts = accountListExists();
  accountListIsEmpty(accounts);
  accountRepository.deleteAll();
  return true;
 }


 // CUSTOM METHODS
 private Account accountExists (Integer accountId) {
  return accountRepository.findById(accountId)
          .orElseThrow(() -> new AccountException(
                  new ErrorResponse(
                          ErrorCode.ANF,
                          "Account with id " + accountId + " not found")));
 }
 private Set<Account> accountListExists () {
  return new HashSet<>(accountRepository.findAll());
 }
 private void checkNotNullRequest(AddAccountRequest request) {
  if (request == null){
     throw new AccountException(
           new ErrorResponse(
                 ErrorCode.NCA,
                 "Account with " + request.getAccountType() + " is null"));
  }
 }
 private Account initializeAccountFromRequest(AddAccountRequest request) {
  Account account = new Account();
  account.setAccountType(request.getAccountType());
  account.setState(request.getState());
  account.setBalance(request.getBalance());
  return account;
 }
 private void accountListIsEmpty(Set<Account> accounts) {
  if (accounts.isEmpty()) {
   throw new AccountException(
           new ErrorResponse(
                   ErrorCode.NA,
                   "Any account founded inside the list"
           )
   );
  }
 }
 private void validateTransfer(Account sourceAccount, Account destinationAccount) {
  checkAccountState(sourceAccount);
  checkAccountState(destinationAccount);
 }
 private void checkAccountState(Account account) {
  if (account.getState() != State.ACTIVE) {
   throw new AccountException(
           new ErrorResponse(ErrorCode.IAS,
                   "Account with id " + account.getId() + " is not active")
   );
  }
 }
 private void executeTransfer(Account sourceAccount, Account destinationAccount, TransferMoneyRequest request) {
  Label transferOutLabel = labelRepository.findByCategoryLabelMapping_CategoryTypeAndCategoryLabelMapping_AllowedLabelType(
                  CategoryType.UTILITY, LabelType.TRANSFER_OUT)
          .orElseThrow(() -> new LabelException(new ErrorResponse(ErrorCode.LNF, "Transfer out label not found")));

  Label transferInLabel = labelRepository.findByCategoryLabelMapping_CategoryTypeAndCategoryLabelMapping_AllowedLabelType(
                  CategoryType.UTILITY, LabelType.TRANSFER_IN)
          .orElseThrow(() -> new LabelException(new ErrorResponse(ErrorCode.LNF, "Transfer in label not found")));

  // Aggiorna i saldi
  destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
  sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));

  // Salva le transazioni con le rispettive label
  saveSourceTransaction(sourceAccount, destinationAccount, request, transferOutLabel);
  saveDestinationTransaction(sourceAccount, destinationAccount, request, transferInLabel);
 }
 private void saveSourceTransaction(Account sourceAccount, Account destinationAccount, TransferMoneyRequest request, Label transferOutLabel) {
  Transaction outgoingTransaction = new Transaction();
  outgoingTransaction.setAccount(sourceAccount);
  outgoingTransaction.setTransactionDate(LocalDate.now());
  outgoingTransaction.setTransactionType(TransactionType.EXPENSE);
  outgoingTransaction.setAmount(request.getAmount());
  outgoingTransaction.setName(String.format("Transfer to %s", destinationAccount.getAccountType()));
  outgoingTransaction.setBeneficiary(destinationAccount.getAccountType().toString());
  outgoingTransaction.setSource(sourceAccount.getAccountType().toString());
  outgoingTransaction.setRecurring(false);
  outgoingTransaction.setLabel(transferOutLabel);

  transactionRepository.save(outgoingTransaction);
 }
 private void saveDestinationTransaction(Account sourceAccount, Account destinationAccount, TransferMoneyRequest request, Label transferInLabel) {
  Transaction incomingTransaction = new Transaction();
  incomingTransaction.setAccount(destinationAccount);
  incomingTransaction.setTransactionDate(LocalDate.now());
  incomingTransaction.setTransactionType(TransactionType.INCOME);
  incomingTransaction.setAmount(request.getAmount());
  incomingTransaction.setName(String.format("Transfer from %s", sourceAccount.getAccountType()));
  incomingTransaction.setBeneficiary(destinationAccount.getAccountType().toString());
  incomingTransaction.setSource(sourceAccount.getAccountType().toString());
  incomingTransaction.setRecurring(false);
  incomingTransaction.setLabel(transferInLabel);

  transactionRepository.save(incomingTransaction);
 }
 private void checkMajorBalance(Account account, TransferMoneyRequest request) {
  BigDecimal sourceBalance = account.getBalance();
  if (BigDecimalUtils.isGreaterThan(request.getAmount(), sourceBalance)) {
   throw new AccountException(
           new ErrorResponse(ErrorCode.IB,
                   "Account with id " + account.getId() + " doesn't have enough balance"
           ));
  }
 }
}
