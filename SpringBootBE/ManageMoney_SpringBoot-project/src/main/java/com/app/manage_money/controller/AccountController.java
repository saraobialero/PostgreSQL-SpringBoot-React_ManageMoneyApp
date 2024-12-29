package com.app.manage_money.controller;

import com.app.manage_money.model.Account;
import com.app.manage_money.model.dto.response.AccountDTO;
import com.app.manage_money.model.dto.response.SuccessResponse;
import com.app.manage_money.model.dto.response.TransactionDTO;
import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.State;
import com.app.manage_money.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;


    // CREATE
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody Account account) {
        return null;
    }

    // READ
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Integer id) {
        return null;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<Set<AccountDTO>>> getAllAccounts() {
        return new ResponseEntity<>(new SuccessResponse<>(accountService.getAccounts()), HttpStatus.OK);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable Integer id) {
        return null;
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<Map<String, BigDecimal>> getAccountAnalytics(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return null;
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDTO>> getAccountTransactions(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return null;
    }

    @GetMapping("/{id}/validate-transaction")
    public ResponseEntity<Boolean> validateTransaction(
            @PathVariable Integer id,
            @RequestParam BigDecimal amount) {
        return null;
    }

    @GetMapping("/{id}/expenses/by-category")
    public ResponseEntity<Map<CategoryType, BigDecimal>> getExpensesByCategory(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return null;
    }

    @GetMapping("/{id}/expenses/by-label")
    public ResponseEntity<Map<LabelType, BigDecimal>> getExpensesByLabel(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return null;
    }

    // UPDATE
    @PutMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> updateBalance(
            @PathVariable Integer id,
            @RequestParam BigDecimal amount) {
        return null;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<State> updateAccountStatus(
            @PathVariable Integer id,
            @RequestBody State newState) {
        return null;
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAccount(@PathVariable Integer id) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteAllAccounts() {
        return null;
    }
}