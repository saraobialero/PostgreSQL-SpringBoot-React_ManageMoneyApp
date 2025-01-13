package com.app.manage_money.repository;


import com.app.manage_money.model.Account;
import com.app.manage_money.model.Label;
import com.app.manage_money.model.Transaction;
import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    void deleteAllByAccount(Account account);

    List<Transaction> findByAccountId(Integer accountId);

    List<Transaction> findByTransactionType(TransactionType transactionType);

    List<Transaction> findByAccountIdAndTransactionDateBetween(
                                                                Integer accountId,
                                                                LocalDate startDate,
                                                                LocalDate endDate
    );

    List<Transaction> findByTransactionDateBetween(
                                                    LocalDate startDate,
                                                    LocalDate endDate
    );

    List<Transaction> findByAmountBetween(
                                          BigDecimal minAmount,
                                          BigDecimal maxAmount);

    List<Transaction> findByAccountIdAndLabel_CategoryLabelMapping_AllowedLabelTypeAndTransactionTypeAndTransactionDateBetween(
                        Integer accountId,
                        LabelType labelType,
                        TransactionType type,
                        LocalDate startDate,
                        LocalDate endDate
    );

    List<Transaction> findByLabel_CategoryLabelMapping_AllowedLabelType(
            LabelType labelType
    );


    List<Transaction> findByAccountIdAndTransactionDateGreaterThanEqualAndTransactionDateLessThanEqual(
            Integer accountId,
            LocalDate startDate,
            LocalDate endDate);

    List<Transaction> findByAccountIdAndLabel_CategoryLabelMapping_CategoryTypeAndTransactionTypeAndTransactionDateBetween(
            Integer accountId,
            CategoryType category,
            TransactionType transactionType,
            LocalDate startDate,
            LocalDate endDate);

    List<Transaction> findByLabel_CategoryLabelMapping_CategoryType(
            CategoryType category);

    List<Transaction> findByTransactionDateBetweenAndTransactionType(LocalDate start, LocalDate end, TransactionType transactionType);
}