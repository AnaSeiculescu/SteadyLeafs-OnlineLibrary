package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping()
	public String getAllUsers(Model model) {
		List<User> userList = userService.getAllUsers();
		model.addAttribute("userList", userList);
		return "users/usersList";
	}

	@GetMapping("/create")
	public String getCreateUserForm(Model model) {
		model.addAttribute("user", new User());
		return "users/createUserForm";
	}

	@PostMapping()
	public String createUser(Model model, @ModelAttribute User user) {
		userService.createUser(user);
		model.addAttribute("userList", userService.getAllUsers());
		return "users/usersList";
	}

//	@GetMapping("/update")
//	public String getUpdateUserForm(Model model, @RequestBody User user) {
//		model.addAttribute("user", new)
//	}
}
