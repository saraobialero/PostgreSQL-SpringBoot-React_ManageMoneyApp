package com.app.manage_money.model.dto.response;

import com.app.manage_money.model.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LabelDTO {
    private Integer id;
    private CategoryLabelMappingDTO categoryLabelMapping;
    private TransactionType transactionType;
    private boolean isActive;
}