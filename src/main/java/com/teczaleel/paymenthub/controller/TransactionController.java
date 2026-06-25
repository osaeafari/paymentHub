package com.teczaleel.paymenthub.controller;

import com.teczaleel.paymenthub.entity.PaymentTransaction;
import com.teczaleel.paymenthub.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<PaymentTransaction> processTransaction(@RequestBody PaymentTransaction rawPayload) {
        PaymentTransaction processedTx = transactionService.createTransaction(rawPayload);
        return ResponseEntity.ok(processedTx);
    }

    @GetMapping
    public ResponseEntity<List<PaymentTransaction>> fetchHistory() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}