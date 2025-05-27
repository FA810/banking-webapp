package com.bankingwebapp.initializer;

import com.bankingwebapp.entity.User;
import com.bankingwebapp.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDataInitializer extends BaseDataInitializer<User> {

	@Autowired
	private UserRepository userRepository;

	// Define a limit for rows in the database
	private static final int MAX_ROWS = 20;

	@Override
	protected void initializeData() {
		Faker faker = new Faker();

		// Create and save User instances
		for (int i = 0; i < MAX_ROWS; i++) {
			String username = faker.name().username();
			String domain = faker.internet().domainName(); // random domain, e.g. example.com

			User user = new User();
			user.setUsername(username);
			user.setPassword(faker.internet().password());
			user.setRole("USER");
			user.setEmail(username + "@" + domain); // email starts with username
			userRepository.save(user);
		}
	}
}
