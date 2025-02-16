package com.app.manage_money.controller;

import com.app.manage_money.model.dto.request.AddAccountRequest;
import com.app.manage_money.model.dto.request.AddSavingPlanRequest;
import com.app.manage_money.model.dto.request.TransferMoneyRequest;
import com.app.manage_money.model.dto.response.*;
import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.State;
import com.app.manage_money.service.AccountService;
import com.app.manage_money.service.SavingPlanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
@RequiredArgsConstructor
@RestController
@RequestMapping("/saving-plans")
public class SavingPlanController {

    private final SavingPlanService savingPlanService;

    // CREATE
    @PostMapping("/new")
    public ResponseEntity<SuccessResponse<SavingPlanDTO>> createSavingPlan(
            @Valid @RequestBody AddSavingPlanRequest addSavingPlanRequest) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.createSavingPlan(addSavingPlanRequest)),
                HttpStatus.CREATED
        );
    }

    // READ
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<SavingPlanDTO>> getSavingPlanById(
            @PathVariable Integer id) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.getSavingPlanById(id)),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<Set<SavingPlanDTO>>> getAllSavingPlans() {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.getAllSavingPlans()),
                HttpStatus.OK
        );
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<SuccessResponse<Set<SavingPlanDTO>>> getSavingPlansByAccount(
            @PathVariable Integer accountId) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.getSavingPlansByAccount(accountId)),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}/current-amount")
    public ResponseEntity<SuccessResponse<BigDecimal>> getCurrentAmount(
            @PathVariable Integer id) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.getCurrentAmount(id)),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}/progress")
    public ResponseEntity<SuccessResponse<Double>> getProgressPercentage(
            @PathVariable Integer id) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.calculateProgressPercentage(id)),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}/target-reached")
    public ResponseEntity<SuccessResponse<Boolean>> checkTargetReached(
            @PathVariable Integer id) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.checkTargetReached(id)),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}/monthly-required")
    public ResponseEntity<SuccessResponse<BigDecimal>> getRequiredMonthlySaving(
            @PathVariable Integer id) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.calculateRequiredMonthlySaving(id)),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}/estimated-completion")
    public ResponseEntity<SuccessResponse<LocalDate>> getEstimatedCompletionDate(
            @PathVariable Integer id) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.estimateCompletionDate(id)),
                HttpStatus.OK
        );
    }

    // UPDATE
    @PutMapping("/{id}/progress")
    public ResponseEntity<SuccessResponse<BigDecimal>> updateProgress(
            @PathVariable Integer id,
            @Valid @RequestBody BigDecimal contribution) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.updateProgress(id, contribution)),
                HttpStatus.OK
        );
    }

    // UPDATE
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<SuccessResponse<BigDecimal>> withdrawProgress(
            @PathVariable Integer id,
            @Valid @RequestBody BigDecimal contribution) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.withdrawFromSavingPlan(id, contribution)),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/target-amount")
    public ResponseEntity<SuccessResponse<Boolean>> updateTargetAmount(
            @PathVariable Integer id,
            @Valid @RequestBody BigDecimal newTarget) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.updateTargetAmount(id, newTarget)),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/target-date")
    public ResponseEntity<SuccessResponse<Boolean>> updateTargetDate(
            @PathVariable Integer id,
            @Valid @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDate) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.updateTargetDate(id, newDate)),
                HttpStatus.OK
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteSavingPlan(
            @PathVariable Integer id) {
        return new ResponseEntity<>(
                new SuccessResponse<>(savingPlanService.deleteSavingPlan(id)),
                HttpStatus.OK
        );
    }

}