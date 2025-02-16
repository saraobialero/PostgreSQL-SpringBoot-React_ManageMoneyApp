package com.app.manage_money.service.functions;

import com.app.manage_money.model.SavingPlan;
import com.app.manage_money.model.dto.request.AddSavingPlanRequest;
import com.app.manage_money.model.dto.response.SavingPlanDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface SavingPlanFunctions {
    // CREATE
    SavingPlanDTO createSavingPlan(AddSavingPlanRequest addSavingPlanRequest);

    // READ
    SavingPlanDTO getSavingPlanById(Integer planId);
    Set<SavingPlanDTO> getAllSavingPlans();
    Set<SavingPlanDTO> getSavingPlansByAccount(Integer accountId);

    // Analisi
    BigDecimal getCurrentAmount(Integer planId);
    double calculateProgressPercentage(Integer planId);
    boolean checkTargetReached(Integer planId);
    BigDecimal calculateRequiredMonthlySaving(Integer planId);
    LocalDate estimateCompletionDate(Integer planId);

    // UPDATE
    BigDecimal updateProgress(Integer planId, BigDecimal contribution);
    boolean updateTargetAmount(Integer planId, BigDecimal newTarget);
    BigDecimal withdrawFromSavingPlan(Integer planId, BigDecimal amount);
    boolean updateTargetDate(Integer planId, LocalDate newDate);

    // DELETE
    boolean deleteSavingPlan(Integer planId);
}