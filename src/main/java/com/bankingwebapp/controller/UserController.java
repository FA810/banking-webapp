package com.bankingwebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
		return "users/users";
	}

	@GetMapping("/add")
	public String showAddUserForm(Model model) {
		model.addAttribute("user", new User());
		return "users/adduser";
	}

	@PostMapping("/save")
	public String saveUser(@ModelAttribute User user) {
		userService.saveUser(user);
		return "redirect:/users";
	}
}
