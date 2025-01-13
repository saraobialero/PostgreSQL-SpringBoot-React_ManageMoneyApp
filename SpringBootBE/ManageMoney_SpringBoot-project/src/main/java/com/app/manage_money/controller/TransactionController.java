package com.app.manage_money.controller;

import com.app.manage_money.model.Transaction;
import com.app.manage_money.model.dto.request.AddAccountRequest;
import com.app.manage_money.model.dto.request.AddTransactionRequest;
import com.app.manage_money.model.dto.request.TransactionUpdateRequest;
import com.app.manage_money.model.dto.request.TransferMoneyRequest;
import com.app.manage_money.model.dto.response.*;
import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.State;
import com.app.manage_money.model.enums.TransactionType;
import com.app.manage_money.service.AccountService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;


    // CREATE
    @PostMapping
    public ResponseEntity<SuccessResponse<TransactionDTO>> createTransaction(@RequestBody @Valid AddTransactionRequest transactionRequest) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.addTransaction(transactionRequest)), HttpStatus.OK);
    }


    // READ
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<TransactionDTO>> getTransactionById(@PathVariable Integer id) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.getTransactionById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<TransactionDTO>>> getAllTransaction() {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.getAllTransactions()), HttpStatus.OK);
    }

    @GetMapping("/by-range/")
    public ResponseEntity<SuccessResponse<List<TransactionDTO>>> getAllTransactionsByRange( @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.getTransactionsByDateRange(startDate, endDate)), HttpStatus.OK);
    }

    @GetMapping("/by-account/{id}")
    public ResponseEntity<SuccessResponse<List<TransactionDTO>>> getAllTransactionsByAccount(@PathVariable Integer id) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.getAllTransactionsByAccount(id)), HttpStatus.OK);
    }

    @GetMapping("/by-label/")
    public ResponseEntity<SuccessResponse<List<TransactionDTO>>> getAllTransactionsByLabel(@RequestParam LabelType labelType) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.getTransactionsByLabel(labelType)), HttpStatus.OK);
    }

    @GetMapping("/by-category/")
    public ResponseEntity<SuccessResponse<List<TransactionDTO>>> getAllTransactionsByCategory(@RequestParam CategoryType categoryType) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.getTransactionsByCategory(categoryType)), HttpStatus.OK);
    }

    @GetMapping("/expenses/by-range")
    public ResponseEntity<SuccessResponse<BigDecimal>> getAllExpensesByRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.calculateTotalExpenses(startDate, endDate)), HttpStatus.OK);
    }

    @GetMapping("/incomes/by-range")
    public ResponseEntity<SuccessResponse<BigDecimal>> getAllIncomesByRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.calculateTotalIncomes(startDate, endDate)), HttpStatus.OK);
    }

    @GetMapping("/by-amount")
    public ResponseEntity<SuccessResponse<List<TransactionDTO>>> getAllTransactionByAmount(@RequestParam BigDecimal minAmount,
                                                                                           @RequestParam BigDecimal maxAmount) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.getTransactionsByAmount(minAmount, maxAmount)), HttpStatus.OK);
    }

    @GetMapping("/by-type")
    public ResponseEntity<SuccessResponse<List<TransactionDTO>>> getAllTransactionByType(@RequestParam TransactionType transactionType) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.getTransactionsByType(transactionType)), HttpStatus.OK);
    }


    @GetMapping("/analytics/monthly")
    public ResponseEntity<SuccessResponse<TransactionMonthlyAnalyticsDTO>> getMonthlyAnalytics(
            @RequestParam Integer accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(
                new SuccessResponse<>(transactionService.getMonthlyTransactionSummary(accountId, date)),
                HttpStatus.OK
        );
    }


    // UPDATE
    @PutMapping("/{id}/details")
    public ResponseEntity<SuccessResponse<TransactionDTO>> updateBalance(
            @PathVariable Integer id,
            @RequestBody @Valid TransactionUpdateRequest request) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.updateTransactionDetails(id, request)), HttpStatus.OK);
    }

    @PatchMapping("/{id}/categorize")
    public ResponseEntity<SuccessResponse<LabelType>> categorizeTransaction(
            @PathVariable Integer id,
            @RequestParam @NotNull LabelType labelType) {
        return new ResponseEntity<>(
                new SuccessResponse<>(transactionService.categorizeTransaction(id, labelType)),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/type")
    public ResponseEntity<SuccessResponse<TransactionType>> updateTransactionType(
            @PathVariable Integer id,
            @RequestParam @NotNull TransactionType transactionType) {
        return new ResponseEntity<>(
                new SuccessResponse<>(transactionService.updateTransactionType(id, transactionType)),
                HttpStatus.OK
        );
    }


    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteTransaction(@PathVariable Integer id) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.deleteTransactionById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/delete/account/{id}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteTransactionByAccount(@PathVariable Integer id) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.deleteTransactionsByAccount(id)), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponse<Boolean>> deleteTransactionByDateRange(@PathVariable Integer id,
                                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.deleteTransactionsByDateRangeAndAccount(id, startDate, endDate)), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse<Boolean>> deleteAllA() {
        return new ResponseEntity<>(new SuccessResponse<>(transactionService.deleteAll()), HttpStatus.OK);
    }
}