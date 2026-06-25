package com.teczaleel.paymenthub.repository;

import com.teczaleel.paymenthub.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * High-performance access interface executing query sequences over transaction data tables.
 * */
@Repository
public interface TransactionRepository extends JpaRepository<PaymentTransaction, Long>{

    // Custom query method auto-derived by Spring Data based on method name signatures!
    Optional<PaymentTransaction> findByTransactionReference(String reference);
    Optional<PaymentTransaction> findByCurrency(String currency);
}