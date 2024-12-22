package com.app.manage_money.service.functions;

import com.app.manage_money.model.Label;

import java.util.List;

public interface LabelFunctions {
    List<Label> getActiveLabels();
    void deactivateLabel(Integer labelId);
    void activateLabel(Integer labelId);
    List<Label> getLabelsByCategory(String categoryType);

}
