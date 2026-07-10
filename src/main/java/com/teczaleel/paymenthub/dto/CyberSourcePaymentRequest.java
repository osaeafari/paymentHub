package com.teczaleel.paymenthub.dto;

/**
 * Root contractual request object sent to the CyberSource REST /pts/v2/payments API.
 */
public record CyberSourcePaymentRequest(
        ClientReferenceInfo clientReferenceInformation,
        OrderInformation orderInformation,
        PaymentInformation paymentInformation
) {}