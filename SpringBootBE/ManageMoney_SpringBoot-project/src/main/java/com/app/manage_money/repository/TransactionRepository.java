package com.app.manage_money.repository;


import com.app.manage_money.model.Label;
import com.app.manage_money.model.Transaction;
import com.app.manage_money.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByAccountIdAndTransactionDateBetween(
            Integer accountId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<Transaction> findByAccountIdAndTransactionTypeAndTransactionDateBetween(
            Integer accountId,
            TransactionType transactionType,
            LocalDate startDate,
            LocalDate endDate
    );
}
