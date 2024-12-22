package com.app.manage_money.repository;


import com.app.manage_money.model.Label;
import com.app.manage_money.model.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Integer> {
}
