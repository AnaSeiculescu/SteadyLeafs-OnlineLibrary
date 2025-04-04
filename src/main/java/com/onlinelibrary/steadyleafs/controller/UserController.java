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

	@GetMapping("/updateForm")
	public String getUpdateUserForm(Model model, @RequestParam int id) {
		User user = userService.getUserById(id);
		model.addAttribute("user", user);
		model.addAttribute("userId", id);
		return "users/updateUserForm";
	}

	@PostMapping("/update")
	public String updateUser(@RequestParam int id, @ModelAttribute User user) {
		User userToUpdate = userService.getUserById(id);
		userToUpdate = userService.updateUser(user);

		return "redirect:/users";
	}

	@PostMapping("/delete")
	public String deleteUser(@RequestParam int id) {
		userService.deleteUser(id);

		return "redirect:/users";
	}
}
