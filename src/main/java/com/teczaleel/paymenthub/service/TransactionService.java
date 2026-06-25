package com.teczaleel.paymenthub.service;

import com.teczaleel.paymenthub.entity.PaymentTransaction;
import com.teczaleel.paymenthub.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public PaymentTransaction createTransaction(PaymentTransaction tx) {
        // Enforce safe business generation logic for audit trails
        tx.setTransactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        tx.setStatus("PENDING");
        return transactionRepository.save(tx);
    }

    public List<PaymentTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}