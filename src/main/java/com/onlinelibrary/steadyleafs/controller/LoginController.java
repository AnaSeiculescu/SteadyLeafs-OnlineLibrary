package com.onlinelibrary.steadyleafs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login-member")
	public String memberLoginPage(Model model) {
		model.addAttribute("loginRole", "MEMBER");
		return "login/login-member";
	}

	@GetMapping("/login-librarian")
	public String librarianLoginPage(Model model) {
		model.addAttribute("loginRole", "LIBRARIAN");
		return "login/login-librarian";
	}

}
