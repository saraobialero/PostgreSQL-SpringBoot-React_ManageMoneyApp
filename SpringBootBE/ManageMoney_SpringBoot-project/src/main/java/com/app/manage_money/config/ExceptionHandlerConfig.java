package com.app.manage_money.config;

import com.app.manage_money.exception.*;
import com.app.manage_money.model.dto.response.ErrorResponse;
import com.app.manage_money.model.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@EnableWebMvc
@RestControllerAdvice
public class ExceptionHandlerConfig {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> handleAccountException(AccountException e) {
        logStackTrace(e.getResponse(), e);
        return new ResponseEntity<>(e.getResponse(), e.getResponse().getStatus());
    }

    @ExceptionHandler(LabelException.class)
    public ResponseEntity<ErrorResponse> handleLabelException(LabelException e) {
        logStackTrace(e.getResponse(), e);
        return new ResponseEntity<>(e.getResponse(), e.getResponse().getStatus());
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ErrorResponse> handleTransactionException(TransactionException e) {
        logStackTrace(e.getResponse(), e);
        return new ResponseEntity<>(e.getResponse(), e.getResponse().getStatus());
    }

    @ExceptionHandler(RecurringTransactionException.class)
    public ResponseEntity<ErrorResponse> handleRecurringTransactionException(RecurringTransactionException e) {
        logStackTrace(e.getResponse(), e);
        return new ResponseEntity<>(e.getResponse(), e.getResponse().getStatus());
    }

    @ExceptionHandler(SavingPlanException.class)
    public ResponseEntity<ErrorResponse> handleSavingPlanException(SavingPlanException e) {
        logStackTrace(e.getResponse(), e);
        return new ResponseEntity<>(e.getResponse(), e.getResponse().getStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BR, getMessage(e));
        logStackTrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for (ObjectError error : result.getAllErrors()) {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BR, errors);
        logStackTrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BR, getMessage(e));
        logStackTrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BR, getMessage(e));
        logStackTrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ANF, getMessage(e));
        logStackTrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ISE, getMessage(e));
        logStackTrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    private void logStackTrace(ErrorResponse errorResponse, Exception e) {
        log.error("Error Code: {} - Status: {} - Message: {}",
                errorResponse.getErrorCode(),
                errorResponse.getStatus(),
                errorResponse.getMessage());
        for (StackTraceElement element : Arrays.copyOfRange(e.getStackTrace(), 0, Math.min(e.getStackTrace().length, 20))) {
            log.error(element.toString());
        }
    }

    private String getMessage(Exception e) {
        return e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : null;
    }
}