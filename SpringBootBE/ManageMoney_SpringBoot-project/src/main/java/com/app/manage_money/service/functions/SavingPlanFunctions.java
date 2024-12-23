package com.app.manage_money.service.functions;

import com.app.manage_money.model.SavingPlan;
import com.app.manage_money.model.dto.SavingPlanDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SavingPlanFunctions {
    // CREATE
    SavingPlanDTO createSavingPlan(SavingPlan savingPlan);

    // READ
    SavingPlanDTO getSavingPlanById(Integer planId);
    List<SavingPlanDTO> getAllSavingPlans();
    List<SavingPlanDTO> getSavingPlansByAccount(Integer accountId);

    // Analisi
    BigDecimal getCurrentAmount(Integer planId);
    double calculateProgressPercentage(Integer planId);
    boolean checkTargetReached(Integer planId);
    BigDecimal calculateRequiredMonthlySaving(Integer planId);
    LocalDate estimateCompletionDate(Integer planId);

    // UPDATE
    BigDecimal updateProgress(Integer planId, BigDecimal contribution);
    boolean updateTargetAmount(Integer planId, BigDecimal newTarget);
    boolean updateTargetDate(Integer planId, LocalDate newDate);

    // DELETE
    boolean deleteSavingPlan(Integer planId);
    boolean deleteSavingPlansByAccount(Integer accountId);
}