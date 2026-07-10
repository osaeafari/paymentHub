package com.teczaleel.paymenthub.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Clean immutable data carrier record representing an inbound payment intent request.
 */
public record TransactionRequest(

        @NotNull(message = "Transaction amount is required")
        @DecimalMin(value = "0.01", message = "Transaction amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Currency identifier is required")
        @Size(min = 3, max = 3, message = "Currency must be an explicit 3-letter ISO code (e.g., USD)")
        String currency
) {}