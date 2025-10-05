package com.bankingwebapp.repository;

import com.bankingwebapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByAmountGreaterThanEqual(BigDecimal amount);

    List<Transaction> findByDescriptionContainingIgnoreCase(String keyword);
}
