package com.app.manage_money.controller;

import com.app.manage_money.model.RecurringTransaction;
import com.app.manage_money.model.dto.request.AddRecurringTransactionRequest;

import com.app.manage_money.model.dto.request.UpdateRecurringTransactionRequest;
import com.app.manage_money.model.dto.response.RecurringTransactionDTO;
import com.app.manage_money.model.dto.response.SuccessResponse;

import com.app.manage_money.service.RecurringTransactionService;
import com.app.manage_money.service.TransactionService;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/recurring-transactions")
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;

    // CREATE
    @PostMapping
    public ResponseEntity<SuccessResponse<RecurringTransactionDTO>> createTransaction(
            @RequestBody @Valid AddRecurringTransactionRequest addRecurringTransactionRequest) {
        return new ResponseEntity<>(
                new SuccessResponse<>(recurringTransactionService.addRecurringTransaction(addRecurringTransactionRequest)),
                HttpStatus.CREATED
        );
    }

    // READ
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<RecurringTransactionDTO>> getRecurringTransactionById(
            @PathVariable Integer id) {
        return new ResponseEntity<>(
                new SuccessResponse<>(recurringTransactionService.getRecurringTransactionById(id)),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<RecurringTransactionDTO>>> getAllRecurringTransactions() {
        return new ResponseEntity<>(
                new SuccessResponse<>(recurringTransactionService.getAllRecurringTransactions()),
                HttpStatus.OK
        );
    }

    @GetMapping("/due")
    public ResponseEntity<SuccessResponse<List<RecurringTransactionDTO>>> getDueTransactions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(
                new SuccessResponse<>(recurringTransactionService.getDueTransactions(date)),
                HttpStatus.OK
        );
    }

    @GetMapping("/upcoming")
    public ResponseEntity<SuccessResponse<List<RecurringTransactionDTO>>> getUpcomingTransactions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(
                new SuccessResponse<>(recurringTransactionService.getUpcomingTransactions(date)),
                HttpStatus.OK
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<RecurringTransactionDTO>> updateRecurringTransaction(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateRecurringTransactionRequest updateRequest) {
        return new ResponseEntity<>(
                new SuccessResponse<>(recurringTransactionService.updateRecurringTransaction(id, updateRequest)),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<SuccessResponse<Boolean>> toggleRecurringTransactionStatus(
            @PathVariable Integer id,
            @RequestParam boolean active) {
        return new ResponseEntity<>(
                new SuccessResponse<>(recurringTransactionService.toggleRecurringTransactionStatus(id, active)),
                HttpStatus.OK
        );
    }



}