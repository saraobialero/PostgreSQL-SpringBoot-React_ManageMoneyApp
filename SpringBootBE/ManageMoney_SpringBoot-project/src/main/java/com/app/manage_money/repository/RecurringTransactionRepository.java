package com.app.manage_money.repository;


import com.app.manage_money.model.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Integer> {
    List<RecurringTransaction> findByIsActiveTrueAndNextOccurrenceBefore(LocalDate currentDate);
    List<RecurringTransaction> findByIsActiveTrueAndNextOccurrenceGreaterThan(LocalDate currentDate);
}
