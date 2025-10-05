package com.bankingwebapp.initializer;

import com.bankingwebapp.entity.BankAccount;
import com.bankingwebapp.entity.Transaction;
import com.bankingwebapp.entity.User;
import com.bankingwebapp.repository.BankAccountRepository;
import com.bankingwebapp.repository.TransactionRepository;
import com.bankingwebapp.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

@Component
public class TransactionDataInitializer extends BaseDataInitializer<Transaction> {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BankAccountRepository bankAccountRepository;

	// Define a limit for rows in the database
	private static final int MAX_ROWS = 20;

	@Override
	protected void initializeData() {
		Faker faker = new Faker();

		// Create and save Transaction instances
		for (int i = 0; i < MAX_ROWS; i++) {
			Transaction transaction = new Transaction();
			// Set the amount using BigDecimal
			transaction.setAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 500)));
			transaction.setDescription(faker.lorem().sentence());
			transaction.setTransactionDate(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).toInstant()
					.atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());

			// Fetch a random user from the database
			User randomUser = userRepository.findById(faker.number().numberBetween(1, 10L))
					.orElseThrow(() -> new RuntimeException("Random User not found"));

			transaction.setUser(randomUser);
			
			// Find all accounts for this user
            List<BankAccount> accounts = bankAccountRepository.findByUserId(randomUser.getId());
            if (!accounts.isEmpty()) {
                BankAccount randomAccount = accounts.get(faker.random().nextInt(accounts.size()));
                transaction.setAccount(randomAccount);
            } else {
                continue; // Skip if user has no accounts
            }

            transaction.setType(faker.options().option("DEPOSIT", "WITHDRAWAL"));

			transactionRepository.save(transaction);
		}
	}
}
