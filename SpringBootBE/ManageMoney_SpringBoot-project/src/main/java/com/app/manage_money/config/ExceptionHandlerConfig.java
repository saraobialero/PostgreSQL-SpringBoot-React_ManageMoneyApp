package com.app.manage_money.config;

import com.app.manage_money.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Date;

@EnableWebMvc
@RestControllerAdvice
public class ExceptionHandlerConfig {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> handleAccountException(AccountException e) {
        logError(e);
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LabelException.class)
    public ResponseEntity<ErrorResponse> handleLabelException(LabelException e) {
        logError(e);
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ErrorResponse> handleTransactionException(TransactionException e) {
        logError(e);
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecurringTransactionException.class)
    public ResponseEntity<ErrorResponse> handleRecurringTransactionException(RecurringTransactionException e) {
        logError(e);
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SavingPlanException.class)
    public ResponseEntity<ErrorResponse> handleSavingPlanException(SavingPlanException e) {
        logError(e);
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                "Internal Server Error",
                e.getMessage()
        );
        logError(e);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void logError(Exception e) {
        log.error("Exception: {} - Message: {}", e.getClass().getSimpleName(), e.getMessage());
        log.error("StackTrace: ", e);
    }
}
