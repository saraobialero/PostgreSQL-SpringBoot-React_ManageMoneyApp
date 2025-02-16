package com.app.manage_money.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    // General errors
    FB("Forbidden", HttpStatus.FORBIDDEN, ExitCode.KO),
    UA("Unauthorized", HttpStatus.UNAUTHORIZED, ExitCode.KO),
    ISE("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, ExitCode.KO),
    BR("Bad request", HttpStatus.BAD_REQUEST, ExitCode.KO),
    ID("Invalid date", HttpStatus.BAD_REQUEST, ExitCode.KO),


    // Account errors
    ANF("Account not found", HttpStatus.NOT_FOUND, ExitCode.KO),
    NA("There aren't any account inside the list", HttpStatus.NO_CONTENT, ExitCode.KO),
    AIA("Account is inactive", HttpStatus.BAD_REQUEST, ExitCode.KO),
    AIB("Account is blocked", HttpStatus.BAD_REQUEST, ExitCode.KO),
    AIC("Account is closed", HttpStatus.BAD_REQUEST, ExitCode.KO),
    IB("Insufficient balance", HttpStatus.BAD_REQUEST, ExitCode.KO),
    NCA("No account provided to add", HttpStatus.BAD_REQUEST, ExitCode.KO),
    IAS("Invalid account state", HttpStatus.BAD_REQUEST, ExitCode.KO),

    // Label errors
    LNF("Label not found", HttpStatus.NOT_FOUND, ExitCode.KO),
    LIA("Label is inactive", HttpStatus.BAD_REQUEST, ExitCode.KO),
    ILM("Invalid label mapping", HttpStatus.BAD_REQUEST, ExitCode.KO),

    // Transaction errors
    TNF("Transaction not found", HttpStatus.NOT_FOUND, ExitCode.KO),
    NCTS("No content inside transactions", HttpStatus.NO_CONTENT, ExitCode.KO),
    ITA("Invalid transaction amount", HttpStatus.BAD_REQUEST, ExitCode.KO),
    NCT("No transaction provided to add", HttpStatus.BAD_REQUEST, ExitCode.KO),
    ITD("Invalid transaction date", HttpStatus.BAD_REQUEST, ExitCode.KO),

    // RecurringTransaction errors
    RTNF("Recurring transaction not found", HttpStatus.NOT_FOUND, ExitCode.KO),
    RTIA("Recurring transaction is inactive", HttpStatus.BAD_REQUEST, ExitCode.KO),
    RTIE("Recurring transaction is expired", HttpStatus.BAD_REQUEST, ExitCode.KO),
    RTNC("No recurring transaction inside the list", HttpStatus.NO_CONTENT, ExitCode.KO),

    // SavingPlan errors
    SPNF("Saving plan not found", HttpStatus.NOT_FOUND, ExitCode.KO),
    SPTA("Saving plan target already reached", HttpStatus.BAD_REQUEST, ExitCode.KO),
    SPIE("Saving plan is expired", HttpStatus.BAD_REQUEST, ExitCode.KO),
    NCSP("No Saving plan provided to add", HttpStatus.BAD_REQUEST, ExitCode.KO),
    NSP("There aren't any saving plan inside the list", HttpStatus.NO_CONTENT, ExitCode.KO),
    IT("Invalid target", HttpStatus.BAD_REQUEST, ExitCode.KO),
    IC("Invalid contribution", HttpStatus.BAD_REQUEST, ExitCode.KO),
    IW("Withdrawal amount must be greater than zero", HttpStatus.BAD_REQUEST, ExitCode.KO);

    private String message;
    private HttpStatus status;
    private ExitCode exitCode;
}
