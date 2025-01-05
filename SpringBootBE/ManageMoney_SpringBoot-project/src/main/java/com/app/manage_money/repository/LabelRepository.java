package com.app.manage_money.repository;


import com.app.manage_money.model.Label;
import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.LabelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Integer> {
    Optional<Label> findByCategoryLabelMapping_CategoryTypeAndCategoryLabelMapping_AllowedLabelType(
            CategoryType categoryType,
            LabelType labelType
    );
}
