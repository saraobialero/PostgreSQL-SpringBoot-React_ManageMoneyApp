package com.app.manage_money.controller;

import com.app.manage_money.model.Account;
import com.app.manage_money.model.dto.request.AddAccountRequest;
import com.app.manage_money.model.dto.request.TransferMoneyRequest;
import com.app.manage_money.model.dto.response.AccountAnalyticsDTO;
import com.app.manage_money.model.dto.response.AccountDTO;
import com.app.manage_money.model.dto.response.SuccessResponse;
import com.app.manage_money.model.dto.response.TransactionDTO;
import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.State;
import com.app.manage_money.service.AccountService;
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
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;


    // CREATE
    @PostMapping
    public ResponseEntity<SuccessResponse<AccountDTO>> createAccount(@RequestBody @Valid AddAccountRequest accountRequest) {
        return new ResponseEntity<>(new SuccessResponse<>(accountService.addAccount(accountRequest)), HttpStatus.OK);
    }


    // READ
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<AccountDTO>> getAccountById(@PathVariable Integer id) {
        return new ResponseEntity<>(new SuccessResponse<>(accountService.getAccountById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<Set<AccountDTO>>> getAllAccounts() {
        return new ResponseEntity<>(new SuccessResponse<>(accountService.getAccounts()), HttpStatus.OK);
    }

    @GetMapping("/total-balance")
    public ResponseEntity<SuccessResponse<BigDecimal>> getAccountsBalance() {
        return new ResponseEntity<>(new SuccessResponse<>(accountService.calculateTotalBalance()), HttpStatus.OK);
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<SuccessResponse<AccountAnalyticsDTO>> getAccountAnalytics(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return new ResponseEntity<>(
                new SuccessResponse<>(accountService.getAccountAnalytics(id, startDate, endDate)),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<SuccessResponse<List<TransactionDTO>>> getAccountTransactions(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return new ResponseEntity<>(
                new SuccessResponse<>(accountService.getAccountTransactions(id, startDate, endDate)),
                HttpStatus.OK
        );
    }

    @GetMapping("/{accountId}/expenses/by-category")
    public ResponseEntity<SuccessResponse<List<TransactionDTO>>> getExpensesByCategory(
            @PathVariable Integer accountId,
            @RequestParam CategoryType category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        accountService.getExpensesByAccountAndCategory(accountId, category, startDate, endDate)
                )
        );
    }

    @GetMapping("/{id}/expenses/by-label")
    public ResponseEntity<SuccessResponse<List<TransactionDTO>>> getExpensesByLabelType(
            @PathVariable Integer id,
            @RequestParam LabelType labelType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        accountService.getExpensesByAccountAndLabelType(id, labelType, startDate, endDate)
                )
        );
    }



    // UPDATE
    @PatchMapping("/{id}/balance")
    public ResponseEntity<SuccessResponse<BigDecimal>> updateBalance(
            @PathVariable Integer id,
            @RequestParam BigDecimal amount) {
        return new ResponseEntity<>(new SuccessResponse<>(accountService.updateBalance(id, amount)), HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SuccessResponse<State>> updateAccountStatus(
            @PathVariable Integer id,
            @RequestParam State newState) {
        return new ResponseEntity<>(new SuccessResponse<>(accountService.updateAccountStatus(id, newState)), HttpStatus.OK);
    }

    @PutMapping("/{sourceId}/transfer/{destinationId}")
    public ResponseEntity<SuccessResponse<Set<AccountDTO>>> transferMoney(
            @PathVariable @NotNull Integer sourceId,
            @PathVariable @NotNull Integer destinationId,
            @Valid @RequestBody TransferMoneyRequest request) {

        return new ResponseEntity<>(new SuccessResponse<>(accountService.transferMoney(sourceId, destinationId, request)), HttpStatus.OK);
    }


    // DELETE
    @PatchMapping("/{id}/state")
    public ResponseEntity<Boolean> updateAccountState(
            @PathVariable Integer id,
            @RequestParam State state) {
        return ResponseEntity.ok(accountService.toggleAccountStatus(id, state));
    }

    @GetMapping("/{id}/active")
    public ResponseEntity<Boolean> isAccountActive(@PathVariable Integer id) {
        return ResponseEntity.ok(accountService.isAccountActive(id));
    }
}