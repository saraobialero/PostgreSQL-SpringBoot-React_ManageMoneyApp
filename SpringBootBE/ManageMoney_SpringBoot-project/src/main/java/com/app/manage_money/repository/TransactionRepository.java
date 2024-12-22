package com.app.manage_money.repository;


import com.app.manage_money.model.Label;
import com.app.manage_money.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
