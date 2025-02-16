package com.app.manage_money.service;

import com.app.manage_money.exception.AccountException;
import com.app.manage_money.exception.SavingPlanException;
import com.app.manage_money.model.Account;
import com.app.manage_money.model.SavingPlan;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
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
        Account account = getAccount(addSavingPlanRequest.getAccountId());

        if (addSavingPlanRequest.getCurrentAmount().compareTo(account.getBalance()) > 0) {
            throw new SavingPlanException(
                    new ErrorResponse(
                            ErrorCode.IB,
                            "Current amount requested (" + addSavingPlanRequest.getCurrentAmount() +
                                    ") is greater than account balance (" + account.getBalance() + ")"
                    )
            );
        }
        account.setBalance(account.getBalance().subtract(addSavingPlanRequest.getCurrentAmount()));
        accountRepository.save(account);
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
        SavingPlan savingPlan = savingPlanExist(planId);
        BigDecimal targetAmount = savingPlan.getTargetAmount();
        BigDecimal currentAmount = savingPlan.getCurrentAmount();

        if (targetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SavingPlanException(
                    new ErrorResponse(
                            ErrorCode.IT,
                            "Target amount must be greater than zero"
                    )
            );
        }

        return currentAmount.divide(targetAmount, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    @Override
    public boolean checkTargetReached(Integer planId) {
        SavingPlan savingPlan = savingPlanExist(planId);
        return savingPlan.getCurrentAmount().compareTo(savingPlan.getTargetAmount()) >= 0;
    }

    @Override
    public BigDecimal calculateRequiredMonthlySaving(Integer planId) {
        SavingPlan savingPlan = savingPlanExist(planId);
        LocalDate now = LocalDate.now();

        if (savingPlan.getTargetDate() == null || savingPlan.getTargetDate().isBefore(now)) {
            throw new SavingPlanException(
                    new ErrorResponse(
                            ErrorCode.ID,
                            "Target date must be in the future"
                    )
            );
        }

        BigDecimal remainingAmount = savingPlan.getTargetAmount()
                .subtract(savingPlan.getCurrentAmount());
        long monthsUntilTarget = ChronoUnit.MONTHS.between(now, savingPlan.getTargetDate());

        if (monthsUntilTarget == 0) {
            return remainingAmount;
        }

        return remainingAmount.divide(BigDecimal.valueOf(monthsUntilTarget), 2, RoundingMode.CEILING);
    }

    @Override
    public LocalDate estimateCompletionDate(Integer planId) {
        SavingPlan savingPlan = savingPlanExist(planId);

        // Se l'obiettivo è già raggiunto, ritorna la data corrente
        if (checkTargetReached(planId)) {
            return LocalDate.now();
        }

        // Calcola la media mensile dei risparmi basata sui dati storici
        // Per ora usiamo un valore fisso di risparmio mensile
        BigDecimal monthlyAverage = BigDecimal.valueOf(1000); // TODO: implementare calcolo reale

        BigDecimal remainingAmount = savingPlan.getTargetAmount()
                .subtract(savingPlan.getCurrentAmount());

        long monthsNeeded = remainingAmount.divide(monthlyAverage, 0, RoundingMode.CEILING).longValue();

        return LocalDate.now().plusMonths(monthsNeeded);
    }

    @Transactional
    @Override
    public BigDecimal updateProgress(Integer planId, BigDecimal contribution) {
        if (contribution.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SavingPlanException(
                    new ErrorResponse(
                            ErrorCode.IC,
                            "Contribution must be greater than zero"
                    )
            );
        }

        SavingPlan savingPlan = savingPlanExist(planId);
        BigDecimal newAmount = savingPlan.getCurrentAmount().add(contribution);
        savingPlan.setCurrentAmount(newAmount);
        savingPlanRepository.save(savingPlan);

        return newAmount;
    }

    @Transactional
    @Override
    public BigDecimal withdrawFromSavingPlan(Integer planId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SavingPlanException(
                    new ErrorResponse(
                            ErrorCode.IW,  // You'll need to add this to ErrorCode enum
                            "Withdrawal amount must be greater than zero"
                    )
            );
        }

        SavingPlan savingPlan = savingPlanExist(planId);

        if (amount.compareTo(savingPlan.getCurrentAmount()) > 0) {
            throw new SavingPlanException(
                    new ErrorResponse(
                            ErrorCode.IC,  // You'll need to add this to ErrorCode enum
                            "Insufficient savings balance. Available: " +
                                    savingPlan.getCurrentAmount() + ", Requested: " + amount
                    )
            );
        }

        BigDecimal newAmount = savingPlan.getCurrentAmount().subtract(amount);
        savingPlan.setCurrentAmount(newAmount);

        // If you want to credit the amount back to the linked account
        Account account = savingPlan.getAccount();
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        savingPlanRepository.save(savingPlan);

        return newAmount;
    }

    @Transactional
    @Override
    public boolean updateTargetAmount(Integer planId, BigDecimal newTarget) {
        if (newTarget.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SavingPlanException(
                    new ErrorResponse(
                            ErrorCode.IT,
                            "Target amount must be greater than zero"
                    )
            );
        }

        SavingPlan savingPlan = savingPlanExist(planId);
        savingPlan.setTargetAmount(newTarget);
        savingPlanRepository.save(savingPlan);

        return true;
    }

    @Transactional
    @Override
    public boolean updateTargetDate(Integer planId, LocalDate newDate) {
        if (newDate.isBefore(LocalDate.now())) {
            throw new SavingPlanException(
                    new ErrorResponse(
                            ErrorCode.ID,
                            "Target date must be in the future"
                    )
            );
        }

        SavingPlan savingPlan = savingPlanExist(planId);
        savingPlan.setTargetDate(newDate);
        savingPlanRepository.save(savingPlan);

        return true;
    }

    @Transactional
    @Override
    public boolean deleteSavingPlan(Integer planId) {
        SavingPlan savingPlan = savingPlanExist(planId);
        savingPlanRepository.delete(savingPlan);
        return true;
    }


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
