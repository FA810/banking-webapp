package com.bankingwebapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bankingwebapp.entity.User;
import com.bankingwebapp.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
    @GetMapping("/login")
    public String showLoginPage() {
        return "login.html"; // This will map to user-login.html
    }

    @PostMapping("/login")
    public String loginUser(String username, String password, Model model) {
        Optional<User> user = userService.getUserByUsername(username); // Using getUserByUsername

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            model.addAttribute("user", user.get());
            return "user-dashboard"; // Redirect to a dashboard after login
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login"; // Stay on the same page with an error message
        }
    }
    
    @GetMapping
    public String showUsersPage(Model model) {
        model.addAttribute("users", userService.getAllUsers()); // Adds users to the view
        return "users"; 
    }
}

