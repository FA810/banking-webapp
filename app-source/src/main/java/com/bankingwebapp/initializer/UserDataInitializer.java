package com.bankingwebapp.initializer;

import com.bankingwebapp.entity.User;
import com.bankingwebapp.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserDataInitializer extends BaseDataInitializer<User> {

    @Autowired
    private UserRepository userRepository;

    private static final int MAX_ROWS = 60;

    @Override
    protected void initializeData() {
        if (userRepository.count() >= MAX_ROWS) return;

        // Predefined users
        List<User> predefinedUsers = Arrays.asList(
            createUser("admin", "admin123", "ADMIN", "admin@bank.com"),
            createUser("fabz", "fabz", "ADMIN", "fabz@bank.com")
        );

        predefinedUsers.forEach(user -> {
            if (!userRepository.existsByUsername(user.getUsername())) {
                userRepository.save(user);
            }
        });

        long remaining = MAX_ROWS - userRepository.count();
        Faker faker = new Faker();

        for (int i = 0; i < remaining; i++) {
            String username = faker.name().username();
            String domain = faker.internet().domainName();
            User user = createUser(
                username,
                faker.internet().password(),
                "USER",
                username + "@" + domain
            );
            if (!userRepository.existsByUsername(user.getUsername())) {
                userRepository.save(user);
            }
        }
    }

    private User createUser(String username, String password, String role, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setEmail(email);
        return user;
    }
}
