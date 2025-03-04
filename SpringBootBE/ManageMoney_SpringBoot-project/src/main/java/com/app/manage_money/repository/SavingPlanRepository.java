package com.app.manage_money.repository;


import com.app.manage_money.model.Account;
import com.app.manage_money.model.Label;
import com.app.manage_money.model.SavingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SavingPlanRepository extends JpaRepository<SavingPlan, Integer> {
    void deleteAllByAccount(Account account);

    Set<SavingPlan> findByAccountId(Integer accountId);
}
