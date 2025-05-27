package com.bankingwebapp.initializer;

import com.bankingwebapp.entity.BankAccount;
import com.bankingwebapp.entity.User;
import com.bankingwebapp.repository.BankAccountRepository;
import com.bankingwebapp.repository.UserRepository;
import com.github.javafaker.Faker;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankAccountDataInitializer extends BaseDataInitializer<BankAccount> {

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private UserRepository userRepository;

	// Define a limit for rows in the database
	private static final int MAX_ROWS = 20;

	@Override
	protected void initializeData() {
		Faker faker = new Faker();

		// Create and save BankAccount instances
		for (int i = 0; i < MAX_ROWS; i++) {
			BankAccount bankAccount = new BankAccount();
			bankAccount.setAccountNumber(faker.finance().creditCard());
			bankAccount.setBalance(BigDecimal.valueOf(faker.number().randomDouble(2, 100, 1000)));
			bankAccount.setAccountType("Savings");

			// Fetch a random user from the database
			User randomUser = userRepository.findById(faker.number().numberBetween(1, 10L))
					.orElseThrow(() -> new RuntimeException("Random User not found"));

			bankAccount.setUser(randomUser);

			bankAccountRepository.save(bankAccount);
		}
	}
}
