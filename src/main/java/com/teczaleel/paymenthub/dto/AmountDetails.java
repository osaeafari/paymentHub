package com.teczaleel.paymenthub.dto;

public record AmountDetails(
        String totalAmount, // CyberSource demands amount values formatted directly as precise text strings
        String currency
) {}