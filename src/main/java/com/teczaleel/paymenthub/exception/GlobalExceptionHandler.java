package com.teczaleel.paymenthub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized exception translation layer capturing and formatting application error flows.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Intercepts validation failures and transforms them into a targeted field-error dictionary map.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponseBody = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        // Extract each validation message we defined on our TransactionRequest record fields
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        // Assemble our developer-friendly error response structure
        errorResponseBody.put("timestamp", LocalDateTime.now().toString());
        errorResponseBody.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponseBody.put("error", "Validation Failed");
        errorResponseBody.put("details", fieldErrors);

        return new ResponseEntity<>(errorResponseBody, HttpStatus.BAD_REQUEST);
    }
}