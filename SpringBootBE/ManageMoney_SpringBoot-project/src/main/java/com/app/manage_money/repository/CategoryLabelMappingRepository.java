package com.app.manage_money.repository;

import com.app.manage_money.model.CategoryLabelMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryLabelMappingRepository extends JpaRepository<CategoryLabelMapping, Integer> {
}
