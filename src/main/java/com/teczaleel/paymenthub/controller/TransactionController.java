package com.teczaleel.paymenthub.controller;

import com.teczaleel.paymenthub.dto.TransactionRequest;
import com.teczaleel.paymenthub.entity.PaymentTransaction;
import com.teczaleel.paymenthub.service.PaymentService;
import com.teczaleel.paymenthub.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final PaymentService paymentService;

    public TransactionController(TransactionService transactionService, PaymentService paymentService) {
        this.transactionService = transactionService;
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentTransaction> processTransaction(@Valid @RequestBody TransactionRequest rawPayload) {
        PaymentTransaction processedTx = paymentService.processPayment(rawPayload);
        return ResponseEntity.ok(processedTx);
    }

    @GetMapping
    public ResponseEntity<List<PaymentTransaction>> fetchHistory() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}