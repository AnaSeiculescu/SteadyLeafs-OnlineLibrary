package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.dto.RegistrationDto;
import com.onlinelibrary.steadyleafs.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

	private final UserService userService;

	@GetMapping()
	public String getCreateUserForm(Model model) {
		RegistrationDto registrationDto = new RegistrationDto();
		model.addAttribute("registrationDto", registrationDto);

		return "login/registerUserForm";
	}

	@PostMapping("/create")
	public String createUser(@ModelAttribute @Valid RegistrationDto registrationDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "login/registerUserForm";
		}
		userService.createUser(registrationDto);

		return "redirect:/login-member";
	}

}
