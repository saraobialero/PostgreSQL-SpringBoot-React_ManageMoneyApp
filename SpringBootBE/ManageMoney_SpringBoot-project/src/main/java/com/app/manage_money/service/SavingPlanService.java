package com.app.manage_money.service;

import com.app.manage_money.exception.AccountException;
import com.app.manage_money.exception.SavingPlanException;
import com.app.manage_money.model.Account;
import com.app.manage_money.model.SavingPlan;
import com.app.manage_money.model.dto.request.AddAccountRequest;
import com.app.manage_money.model.dto.request.AddSavingPlanRequest;
import com.app.manage_money.model.dto.response.ErrorResponse;
import com.app.manage_money.model.dto.response.SavingPlanDTO;
import com.app.manage_money.model.enums.ErrorCode;
import com.app.manage_money.repository.AccountRepository;
import com.app.manage_money.repository.SavingPlanRepository;
import com.app.manage_money.service.functions.SavingPlanFunctions;
import com.app.manage_money.utils.DTOConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.app.manage_money.utils.DTOConverter.convertCollection;
import static com.app.manage_money.utils.DTOConverter.convertToSavingPlanDTO;

@RequiredArgsConstructor
@Service
public class SavingPlanService implements SavingPlanFunctions {
    private final SavingPlanRepository savingPlanRepository;
    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public SavingPlanDTO createSavingPlan(AddSavingPlanRequest addSavingPlanRequest) {
        checkNotNullRequest(addSavingPlanRequest);
        SavingPlan savingPlan = initializeSavingPlanFromRequest(addSavingPlanRequest);
        savingPlanRepository.save(savingPlan);
        return convertToSavingPlanDTO(savingPlan);
    }


    @Override
    public SavingPlanDTO getSavingPlanById(Integer planId) {
        SavingPlan savingPlan = savingPlanExist(planId);
        return convertToSavingPlanDTO(savingPlan);
    }

    @Override
    public Set<SavingPlanDTO> getAllSavingPlans() {
        Set<SavingPlan> savingPlans = savingPlanListExists();
        savingPlanListIsEmpty(savingPlans);
        return convertCollection(savingPlans, DTOConverter::convertToSavingPlanDTO, HashSet::new);
    }

    @Override
    public Set<SavingPlanDTO> getSavingPlansByAccount(Integer accountId) {
        Set<SavingPlan> savingPlans = savingPlanRepository.findByAccountId(accountId);
        return convertCollection(savingPlans, DTOConverter::convertToSavingPlanDTO, HashSet::new);
    }

    @Override
    public BigDecimal getCurrentAmount(Integer planId) {
        SavingPlan savingPlan = savingPlanExist(planId);
        return savingPlan.getCurrentAmount();
    }

    @Override
    public double calculateProgressPercentage(Integer planId) {
        return 0;
    }

    @Override
    public boolean checkTargetReached(Integer planId) {
        return false;
    }

    @Override
    public BigDecimal calculateRequiredMonthlySaving(Integer planId) {
        return null;
    }

    @Override
    public LocalDate estimateCompletionDate(Integer planId) {
        return null;
    }

    @Override
    public BigDecimal updateProgress(Integer planId, BigDecimal contribution) {
        return null;
    }

    @Override
    public boolean updateTargetAmount(Integer planId, BigDecimal newTarget) {
        return false;
    }

    @Override
    public boolean updateTargetDate(Integer planId, LocalDate newDate) {
        return false;
    }

    @Override
    public boolean deleteSavingPlan(Integer planId) {
        return false;
    }

    @Override
    public boolean deleteSavingPlansByAccount(Integer accountId) {
        return false;
    }
    // CUSTOM METHODS
    // Create class utility with methods (Generics)
    private void checkNotNullRequest(AddSavingPlanRequest request) {
        if (request == null){
            throw new SavingPlanException(
                    new ErrorResponse(
                            ErrorCode.NCSP,
                            "Saving plan for account: " + request.getAccountId()+ " is null"));
        }
    }

    private SavingPlan initializeSavingPlanFromRequest(AddSavingPlanRequest request) {
        SavingPlan savingPlan = new SavingPlan();
        savingPlan.setAccount(getAccount(request.getAccountId()));
        savingPlan.setCurrentAmount(request.getCurrentAmount());
        savingPlan.setTargetAmount(request.getTargetAmount());
        savingPlan.setStartDate(request.getStartDate());
        savingPlan.setTargetDate(request.getTargetDate());
        return savingPlan;
    }

    private Account getAccount (Integer accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException(
                        new ErrorResponse(
                                ErrorCode.ANF,
                                "Account with id " + accountId + " not found")));
    }

    private SavingPlan savingPlanExist (Integer savingPlanId) {
        return savingPlanRepository.findById(savingPlanId)
                .orElseThrow(() -> new SavingPlanException(
                        new ErrorResponse(
                                ErrorCode.SPNF,
                                "Saving Plan with id " + savingPlanId + " not found")));
    }

    private Set<SavingPlan> savingPlanListExists () {
        return new HashSet<>(savingPlanRepository.findAll());
    }

    private void savingPlanListIsEmpty(Set<SavingPlan> savingPlans) {
        if (savingPlans.isEmpty()) {
            throw new SavingPlanException(
                    new ErrorResponse(
                            ErrorCode.NSP,
                            "Any saving plan founded inside the list"
                    )
            );
        }
    }


}
