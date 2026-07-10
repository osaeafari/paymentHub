package com.teczaleel.paymenthub.service;

import com.teczaleel.paymenthub.dto.TransactionRequest;
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

    // REFACTOR: Accept the immutable clean DTO request packet instead of an entity instance
    public PaymentTransaction createTransaction(TransactionRequest request) {
        PaymentTransaction tx = new PaymentTransaction();

        // Map data from the incoming network object safely onto our database model
        tx.setAmount(request.amount());
        tx.setCurrency(request.currency().toUpperCase());

        tx.setProviderReference("NOT_ASSIGNED");

        tx.setTransactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        tx.setStatus("PENDING");

        return transactionRepository.save(tx);
    }

    public List<PaymentTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}