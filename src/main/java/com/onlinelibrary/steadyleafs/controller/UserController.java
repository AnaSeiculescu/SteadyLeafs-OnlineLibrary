package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.RegistrationDto;
import com.onlinelibrary.steadyleafs.model.dto.UserReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.UserUpdateDto;
import com.onlinelibrary.steadyleafs.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

	@GetMapping()
	public String getAllUsers(Model model) {
		List<UserReturnDto> userList = userService.getAllUsers();
		model.addAttribute("userList", userList);

		return "users/usersList";
	}

	@GetMapping("/register")
	public String getCreateUserForm(Model model) {
		RegistrationDto registrationDto = new RegistrationDto();
		model.addAttribute("registrationDto", registrationDto);

		return "users/registerUserForm";
	}

	@PostMapping()
//	@ResponseStatus(value = HttpStatus.CREATED)
	public String createUser(@ModelAttribute @Valid RegistrationDto registrationDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "/users/registerUserForm";
		}
		userService.createUser(registrationDto);

		return "redirect:/login-member";
	}

	@GetMapping("/updateForm")
	public String getUpdateUserForm(Model model, @RequestParam int id) {
		User user = userService.getUserById(id);
		UserUpdateDto userUpdateDto = UserUpdateDto.mapFromUser(user);
		model.addAttribute("userUpdateDto", userUpdateDto);
		model.addAttribute("userId", id);

		return "users/updateUserForm";
	}

	@PostMapping("/update")
	public String updateUser(@ModelAttribute UserUpdateDto userUpdateDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "/users";
		}
//		UserUpdateDto userToUpdate = UserUpdateDto.mapFromUser(userService.getUserById(userUpdateDto.getId()));

		userService.updateUser(userUpdateDto);


		return "redirect:/users";
	}

	@PostMapping("/delete")
	public String deleteUser(@RequestParam int id) {
		userService.deleteUser(id);

		return "redirect:/users";
	}
}
