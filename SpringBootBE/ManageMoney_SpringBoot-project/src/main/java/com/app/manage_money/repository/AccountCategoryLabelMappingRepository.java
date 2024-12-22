package com.app.manage_money.repository;

import com.app.manage_money.model.AccountRecurringTransaction;
import com.app.manage_money.model.CategoryLabelMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCategoryLabelMappingRepository extends JpaRepository<AccountRecurringTransaction, Integer> {
}
