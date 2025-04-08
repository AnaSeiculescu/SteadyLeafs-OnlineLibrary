package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.config.SecurityConfig;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.UserRegistrationDto;
import com.onlinelibrary.steadyleafs.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	@Autowired
	SecurityConfig securityConfig;
//	private PasswordEncoder passwordEncoder;

	@GetMapping()
	public String getAllUsers(Model model) {
		List<User> userList = userService.getAllUsers();
		model.addAttribute("userList", userList);
		return "users/usersList";
	}

	@GetMapping("/create")
	@ResponseStatus(value = HttpStatus.CREATED)
	public String getCreateUserForm(Model model) {
		UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
		model.addAttribute("userRegistrationDto", userRegistrationDto);
		return "users/registerUserForm";
	}

	@PostMapping()
	public String createUser(Model model, @ModelAttribute @Valid UserRegistrationDto userRegistrationDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "/users/registration";
		}
		userService.createUser(userRegistrationDto.mapToUser(securityConfig.delegatingPasswordEncoder()));
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
