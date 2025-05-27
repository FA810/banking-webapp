package com.bankingwebapp.controller;

import com.bankingwebapp.entity.User;
import com.bankingwebapp.entity.Transaction;
import com.bankingwebapp.entity.BankAccount;
import com.bankingwebapp.service.UserService;
import com.bankingwebapp.service.TransactionService;
import com.bankingwebapp.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/")
    public String homePage(Model model) {
        List<User> users = userService.getAllUsers();
        List<Transaction> transactions = transactionService.getAllTransactions();
        List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();

        model.addAttribute("users", users);
        model.addAttribute("transactions", transactions);
        model.addAttribute("bankAccounts", bankAccounts);

        return "home";
    }
}
