package com.app.manage_money.repository;


import com.app.manage_money.model.Account;
import com.app.manage_money.model.AccountRecurringTransaction;
import com.app.manage_money.model.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRecurringTransactionRepository extends JpaRepository<AccountRecurringTransaction, Integer> {
    void deleteAllByAccount(Account account);
}
