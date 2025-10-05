package com.bankingwebapp.api;

import com.bankingwebapp.entity.BankAccount;
import com.bankingwebapp.service.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = { "/api/bankaccounts", "/api/accounts" })
public class BankAccountRestController {

	private final BankAccountService bankAccountService;

	public BankAccountRestController(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}

	@PostMapping
	public BankAccount createBankAccount(@RequestBody BankAccount bankAccount) {
		return bankAccountService.saveBankAccount(bankAccount);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BankAccount> getBankAccountById(@PathVariable Long id) {
		return bankAccountService.getBankAccountById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public List<BankAccount> showBankAccounts() {
		return bankAccountService.getAllBankAccounts();
	}

	@GetMapping("/user/{userId}")
	public List<BankAccount> getBankAccountsByUserId(@PathVariable Long userId) {
		return bankAccountService.getBankAccountsByUserId(userId);
	}

	@DeleteMapping("/{id}")
	public void deleteBankAccount(@PathVariable Long id) {
		bankAccountService.deleteBankAccountById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<BankAccount> updateBankAccount(@PathVariable Long id,
			@RequestBody BankAccount updatedAccount) {
		Optional<BankAccount> existing = bankAccountService.getBankAccountById(id);
		if (existing.isPresent()) {
			updatedAccount.setId(id);
			return ResponseEntity.ok(bankAccountService.saveBankAccount(updatedAccount));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}