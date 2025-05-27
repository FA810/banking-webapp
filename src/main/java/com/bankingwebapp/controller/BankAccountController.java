package com.bankingwebapp.controller;

import com.bankingwebapp.service.BankAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = { "/bankaccounts", "/accounts" })
public class BankAccountController {

	private final BankAccountService bankAccountService;

	public BankAccountController(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}

	@GetMapping
	public String showBankAccounts(Model model) {
		model.addAttribute("accounts", bankAccountService.getAllBankAccounts());
		return "accounts"; // Returns the ".html" Thymeleaf template
	}
	
	@GetMapping("/{id}")
	public String viewBankAccount(@PathVariable Long id, Model model) {
	    bankAccountService.getBankAccountById(id).ifPresent(account -> model.addAttribute("account", account));
	    return "account-detail";
	}
}
