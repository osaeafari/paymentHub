package com.teczaleel.paymenthub.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teczaleel.paymenthub.dto.*;
import com.teczaleel.paymenthub.entity.PaymentTransaction;
import com.teczaleel.paymenthub.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentService {

    private final TransactionRepository repository;
    private final RestClient restClient;
    private final CyberSourceSecurityService securityService;
    private final ObjectMapper objectMapper;

    @Value("${cybersource.mock-enabled:false}")
    private boolean mockEnabled;

    public PaymentService(TransactionRepository repository,
                          RestClient restClient,
                          CyberSourceSecurityService securityService,
                          ObjectMapper objectMapper) {
        this.repository = repository;
        this.restClient = restClient;
        this.securityService = securityService;
        this.objectMapper = objectMapper;
    }

    public PaymentTransaction processPayment(TransactionRequest request) {
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setAmount(request.amount());
        transaction.setCurrency(request.currency());
        transaction.setStatus("PENDING");
        transaction.setTransactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        final PaymentTransaction stagedTxn = repository.save(transaction);

        if (mockEnabled) {
            return runMockGatewayCall(stagedTxn);
        }

        try {
            CyberSourcePaymentRequest gatewayPayload = new CyberSourcePaymentRequest(
                    new ClientReferenceInfo(stagedTxn.getTransactionReference()),
                    new OrderInformation(new AmountDetails(stagedTxn.getAmount().toString(), stagedTxn.getCurrency())),
                    new PaymentInformation(new CardDetails("4111111111111111", "12", "2030"))
            );

            String jsonPayload = objectMapper.writeValueAsString(gatewayPayload);
            String targetUri = "/pts/v2/payments";
            String gmtDate = securityService.getGmtDateTimeString();
            String digest = securityService.calculateDigest(jsonPayload);
            String signatureHeader = securityService.generateSignatureHeader(gmtDate, digest, targetUri);

            String responseBody = restClient.post()
                    .uri(targetUri)
                    .header("Date", gmtDate)
                    .header("Digest", digest)
                    .header("Signature", signatureHeader)
                    .body(jsonPayload)
                    .retrieve()
                    .body(String.class);

            if (responseBody != null && responseBody.contains("AUTHORIZED")) {
                stagedTxn.setStatus("SUCCESS");
            } else {
                stagedTxn.setStatus("DECLINED");
            }

        } catch (Exception e) {
            stagedTxn.setStatus("FAILED");
            System.err.println("Gateway Outbound Execution Error: " + e.getMessage());
        }

        return repository.save(stagedTxn);
    }

    /**
     * Simulates a CyberSource gateway response without making a real network call.
     * Rule: amounts of 1300.00 or above simulate a DECLINED result (e.g. insufficient funds);
     * everything else simulates SUCCESS. Adjust this rule freely for testing edge cases.
     */
    private PaymentTransaction runMockGatewayCall(PaymentTransaction stagedTxn) {
        try {
            Thread.sleep(400); // simulate network latency so the frontend's loading state is visible
        } catch (InterruptedException ignored) {}

        BigDecimal declineThreshold = new BigDecimal("1300.00");

        if (stagedTxn.getAmount().compareTo(declineThreshold) >= 0) {
            stagedTxn.setStatus("DECLINED");
            stagedTxn.setProviderReference("MOCK-DECLINED-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        } else {
            stagedTxn.setStatus("SUCCESS");
            stagedTxn.setProviderReference("MOCK-AUTH-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }

        System.out.println("MOCK MODE: Simulated gateway response -> " + stagedTxn.getStatus());
        return repository.save(stagedTxn);
    }
}