package com.app.manage_money.service.functions;

import java.math.BigDecimal;

public interface SavingPlanFunctions {
    void updateProgress(Integer planId, BigDecimal contribution);
    double calculateProgressPercentage(Integer planId);
    boolean checkTargetReached(Integer planId);
    void adjustTargetAmount(Integer planId, BigDecimal newTarget);

}
