package com.app.manage_money.model.dto;

import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryLabelMappingDTO {
    private Integer id;
    private CategoryType categoryType;
    private LabelType allowedLabelType;
    private TransactionType transactionType;
}