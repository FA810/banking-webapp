package com.bankingwebapp.api;

import com.bankingwebapp.entity.User;
import com.bankingwebapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody User user) {
	    Optional<User> existingByUsername = userService.getUserByUsername(user.getUsername());
	    Optional<User> existingByEmail = userService.getUserByEmail(user.getEmail());

	    if (existingByUsername.isPresent()) {
	        return ResponseEntity.badRequest().body("Username already exists");
	    }

	    if (existingByEmail.isPresent()) {
	        return ResponseEntity.badRequest().body("Email already exists");
	    }

	    User savedUser = userService.saveUser(user);
	    return ResponseEntity.ok(savedUser);
	}

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<User> getAllUsers() {
    	List<User> users = userService.getAllUsers();
        return users;
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> existing = userService.getUserById(id);
        if (existing.isPresent()) {
            updatedUser.setId(id);
            return ResponseEntity.ok(userService.saveUser(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build(); // Return 204 No Content for successful deletion
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
