package com.bankingwebapp.service;

import com.bankingwebapp.entity.BankAccount;
import com.bankingwebapp.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public BankAccount saveBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    public Optional<BankAccount> getBankAccountById(Long id) {
        return bankAccountRepository.findById(id);
    }

    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public List<BankAccount> getBankAccountsByUserId(Long userId) {
        return bankAccountRepository.findByUserId(userId);
    }

    public void deleteBankAccountById(Long id) {
        bankAccountRepository.deleteById(id);
    }
}
