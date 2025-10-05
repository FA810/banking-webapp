package com.bankingwebapp.service;

import com.bankingwebapp.entity.Transaction;
import com.bankingwebapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> getTransactionsByAmount(BigDecimal amount) {
        return transactionRepository.findByAmountGreaterThanEqual(amount);
    }

    public List<Transaction> searchTransactionsByDescription(String keyword) {
        return transactionRepository.findByDescriptionContainingIgnoreCase(keyword);
    }

    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }
}
