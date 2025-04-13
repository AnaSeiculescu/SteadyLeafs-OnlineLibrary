package com.onlinelibrary.steadyleafs.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login-member")
	public String memberLoginPage(Model model) {
		model.addAttribute("loginRole", "MEMBER");
		System.out.println("I AM IN THE LOGIN MEMBER FORM");
		return "login/login-member";
	}

	@GetMapping("/login-librarian")
	public String librarianLoginPage(Model model) {
		model.addAttribute("loginRole", "LIBRARIAN");
		System.out.println("I AM IN THE LOGIN LIBRARIAN FORM");
		return "login/login-librarian";
	}

}
