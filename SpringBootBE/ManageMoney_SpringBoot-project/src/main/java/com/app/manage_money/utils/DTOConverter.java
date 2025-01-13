package com.app.manage_money.utils;


import com.app.manage_money.model.*;
import com.app.manage_money.model.dto.response.*;
import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.TransactionType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
                .account(convertToAccountDTO(transaction.getAccount()))
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

    public static AccountAnalyticsDTO buildAccountAnalyticsDTO(Account account, BigDecimal totalIncome, BigDecimal totalExpenses, LocalDate startDate, LocalDate endDate) {
        return AccountAnalyticsDTO.builder()
                .currentBalance(account.getBalance())
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .cashFlow(totalIncome.subtract(totalExpenses))
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static TransactionMonthlyAnalyticsDTO buildTransactionAnalyticsDTO(
            List<Transaction> monthlyTransactions,
            List<Transaction> previousMonthTransactions,
            YearMonth yearMonth,
            LocalDate startDate,
            LocalDate endDate) {

        return TransactionMonthlyAnalyticsDTO.builder()
                // Totali principali
                .totalIncome(calculateTotalIncome(monthlyTransactions))
                .totalExpenses(calculateTotalExpenses(monthlyTransactions))
                .netBalance(calculateNetBalance(monthlyTransactions))

                // Metriche su transazioni
                .totalTransactions(monthlyTransactions.size())
                .averageTransactionAmount(calculateAverageTransactionAmount(monthlyTransactions))
                .largestExpense(findLargestExpense(monthlyTransactions))
                .largestIncome(findLargestIncome(monthlyTransactions))

                // Analisi per categoria
                .expensesByCategory(calculateExpensesByCategory(monthlyTransactions))
                .incomeByCategory(calculateIncomeByCategory(monthlyTransactions))
                .topExpenseCategory(findTopExpenseCategory(monthlyTransactions))
                .topIncomeCategory(findTopIncomeCategory(monthlyTransactions))

                // Confronto con mese precedente
                .expensesChangeSinceLastMonth(calculateExpensesChange(monthlyTransactions, previousMonthTransactions))
                .incomeChangeSinceLastMonth(calculateIncomeChange(monthlyTransactions, previousMonthTransactions))

                // Date di riferimento
                .period(yearMonth)
                .startDate(startDate)
                .endDate(endDate)

                // Statistiche giornaliere
                .dailyExpenses(calculateDailyExpenses(monthlyTransactions))
                .dailyIncome(calculateDailyIncome(monthlyTransactions))
                .highestExpenseDay(findHighestExpenseDay(monthlyTransactions))
                .highestIncomeDay(findHighestIncomeDay(monthlyTransactions))

                // Metriche ricorrenti
                .recurringExpensesTotal(calculateRecurringExpenses(monthlyTransactions))
                .recurringIncomesTotal(calculateRecurringIncomes(monthlyTransactions))
                .recurringTransactionsCount(countRecurringTransactions(monthlyTransactions))

                // KPIs
                .savingsRate(calculateSavingsRate(monthlyTransactions))
                .expenseToIncomeRatio(calculateExpenseToIncomeRatio(monthlyTransactions))
                .build();
    }

    // Helper per i totali
    private static BigDecimal calculateTotalIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calculateTotalExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calculateNetBalance(List<Transaction> transactions) {
        return calculateTotalIncome(transactions).subtract(calculateTotalExpenses(transactions));
    }

    // Helper per le metriche delle transazioni
    private static BigDecimal calculateAverageTransactionAmount(List<Transaction> transactions) {
        if (transactions.isEmpty()) return BigDecimal.ZERO;
        BigDecimal total = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(transactions.size()), RoundingMode.HALF_UP);
    }

    private static BigDecimal findLargestExpense(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private static BigDecimal findLargestIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    // Helper per l'analisi delle categorie
    private static Map<CategoryType, BigDecimal> calculateExpensesByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        t -> t.getLabel().getCategoryLabelMapping().getCategoryType(),
                        Collectors.reducing(BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add)
                ));
    }

    private static Map<CategoryType, BigDecimal> calculateIncomeByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.INCOME)
                .collect(Collectors.groupingBy(
                        t -> t.getLabel().getCategoryLabelMapping().getCategoryType(),
                        Collectors.reducing(BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add)
                ));
    }

    private static CategoryType findTopExpenseCategory(List<Transaction> transactions) {
        Map<CategoryType, BigDecimal> expensesByCategory = calculateExpensesByCategory(transactions);
        return expensesByCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private static CategoryType findTopIncomeCategory(List<Transaction> transactions) {
        Map<CategoryType, BigDecimal> incomeByCategory = calculateIncomeByCategory(transactions);
        return incomeByCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Helper per i confronti con il mese precedente
    private static BigDecimal calculateExpensesChange(List<Transaction> currentTransactions, List<Transaction> previousTransactions) {
        BigDecimal currentExpenses = calculateTotalExpenses(currentTransactions);
        BigDecimal previousExpenses = calculateTotalExpenses(previousTransactions);

        if (previousExpenses.equals(BigDecimal.ZERO)) return BigDecimal.ZERO;

        return currentExpenses.subtract(previousExpenses)
                .multiply(BigDecimal.valueOf(100))
                .divide(previousExpenses, 2, RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateIncomeChange(List<Transaction> currentTransactions, List<Transaction> previousTransactions) {
        BigDecimal currentIncome = calculateTotalIncome(currentTransactions);
        BigDecimal previousIncome = calculateTotalIncome(previousTransactions);

        if (previousIncome.equals(BigDecimal.ZERO)) return BigDecimal.ZERO;

        return currentIncome.subtract(previousIncome)
                .multiply(BigDecimal.valueOf(100))
                .divide(previousIncome, 2, RoundingMode.HALF_UP);
    }

    // Helper per le statistiche giornaliere
    private static Map<LocalDate, BigDecimal> calculateDailyExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::getTransactionDate,
                        Collectors.reducing(BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add)
                ));
    }

    private static Map<LocalDate, BigDecimal> calculateDailyIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.INCOME)
                .collect(Collectors.groupingBy(
                        Transaction::getTransactionDate,
                        Collectors.reducing(BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add)
                ));
    }

    private static LocalDate findHighestExpenseDay(List<Transaction> transactions) {
        Map<LocalDate, BigDecimal> dailyExpenses = calculateDailyExpenses(transactions);
        return dailyExpenses.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private static LocalDate findHighestIncomeDay(List<Transaction> transactions) {
        Map<LocalDate, BigDecimal> dailyIncome = calculateDailyIncome(transactions);
        return dailyIncome.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Helper per le metriche ricorrenti
    private static BigDecimal calculateRecurringExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.EXPENSE && t.isRecurring())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calculateRecurringIncomes(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.INCOME && t.isRecurring())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static Integer countRecurringTransactions(List<Transaction> transactions) {
        return (int) transactions.stream()
                .filter(Transaction::isRecurring)
                .count();
    }

    // Helper per i KPIs
    private static Double calculateSavingsRate(List<Transaction> transactions) {
        BigDecimal income = calculateTotalIncome(transactions);
        if (income.equals(BigDecimal.ZERO)) return 0.0;

        return calculateNetBalance(transactions)
                .divide(income, 4, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private static Double calculateExpenseToIncomeRatio(List<Transaction> transactions) {
        BigDecimal income = calculateTotalIncome(transactions);
        if (income.equals(BigDecimal.ZERO)) return 0.0;

        return calculateTotalExpenses(transactions)
                .divide(income, 4, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
