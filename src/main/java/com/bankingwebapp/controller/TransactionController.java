package com.bankingwebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bankingwebapp.service.TransactionService;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

	private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    @GetMapping
    public String showTransactionsPage(Model model) {
        model.addAttribute("transactions", transactionService.getAllTransactions());
        return "transactions";
    }
}
