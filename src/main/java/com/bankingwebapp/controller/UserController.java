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
    
    @GetMapping
    public String showUsersPage(Model model) {
        model.addAttribute("users", userService.getAllUsers()); // Adds users to the view
        return "users"; 
    }
}

