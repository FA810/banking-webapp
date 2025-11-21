package com.bankingwebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
		return "users/userform";
	}

	@PostMapping("/update")
	public String updateUser(@ModelAttribute User user) {
		userService.saveUser(user);
		return "redirect:/users";
	}

	@PostMapping("/save")
	public String saveUser(@ModelAttribute User user) {
		userService.saveUser(user);
		return "redirect:/users";
	}
	
	@PostMapping("/delete/{id}")
	public String deleteUser(@PathVariable Long id) {
	    userService.deleteUserById(id);
	    return "redirect:/users";
	}

	@GetMapping("/edit/{id}")
	public String showEditUserForm(@PathVariable Long id, Model model) {
		User user = userService.getUserById(id).orElse(null);

		// 2. If the user exists, add it to the model
		if (user != null) {
			model.addAttribute("user", user);
			// 3. Returns the name of the view (the HTML form page)
			return "users/userform";
		} else {
			// Error handling if the user is not found
			return "redirect:/users";
		}
	}

}
