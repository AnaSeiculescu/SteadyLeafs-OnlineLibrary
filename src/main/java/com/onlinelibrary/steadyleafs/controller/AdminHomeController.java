package com.onlinelibrary.steadyleafs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminHome")
@RequiredArgsConstructor
public class AdminHomeController {

	@GetMapping()
	public String getAdminHomePage() {
		return "admin/home";
	}
}
