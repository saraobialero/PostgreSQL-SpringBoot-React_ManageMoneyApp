package com.app.manage_money.service.functions;

import com.app.manage_money.model.Label;
import com.app.manage_money.model.dto.response.LabelDTO;
import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.TransactionType;

import java.util.List;

public interface LabelFunctions {
    //CREATE
    LabelDTO addLabel (Label label); // How to manage with mapping?

    //READ
    LabelDTO getLabelById (Integer labelId);
    List<LabelDTO> getLabels();
    List<LabelDTO> getActiveLabels();
    List<LabelDTO> getLabelsByCategory(CategoryType categoryType);

    //UPDATE
    boolean deactivateLabel(Integer labelId);
    boolean activateLabel(Integer labelId);
    LabelDTO updateTransactionType (Integer labelId, TransactionType transactionType);
    LabelDTO updateCategoryType (Integer labelId, CategoryType categoryType);
    LabelDTO updateLabelType (Integer labelId, LabelType labelType);

    //DELETE
    boolean deleteLabelById (Integer labelId);
    boolean deleteAll();

    //MAPPING VALIDATION
    boolean isValidLabelForCategory(CategoryType category, LabelType label);
    boolean isValidLabelForTransactionType(LabelType label, TransactionType type);
    List<LabelType> getValidLabelsForCategory(CategoryType category);



}
