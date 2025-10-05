package com.bankingwebapp.service;

import com.bankingwebapp.entity.User;
import com.bankingwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	// Save or Update a User
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	// Retrieve a User by ID
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	// Retrieve All Users
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	// Retrieve a User by Username
	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	// Delete a User by ID
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

	public void createUser(String username, String email, String password, String role) {
		if (userRepository.existsByUsername(username))
			return;

		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password); // Consider encoding the password if using Spring Security
		user.setRole(role);
		userRepository.save(user);

	}
}
