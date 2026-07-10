package com.teczaleel.paymenthub.dto;

public record CardDetails(
        String number,
        String expirationMonth,
        String expirationYear
) {}