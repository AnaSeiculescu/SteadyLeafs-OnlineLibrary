package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.dto.ContactFormDto;
import com.onlinelibrary.steadyleafs.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/email")
public class EmailController {

	@Autowired
	private EmailService emailService;

	@GetMapping("/contact")
	public String getSendEmailForm(Model model) {
		model.addAttribute("form", new ContactFormDto());

		return "onlineLibrary/contact";
	}

	@PostMapping("/send")
	public String sendEmail(@ModelAttribute ContactFormDto form) {
		String toEmail = "anaseiculescu@gmail.com";
		String subject = form.getSubject();
		String body = "Name " + form.getName() + "\n"
					+ "Email " + form.getEmail() + "\n"
					+ "Message " + form.getMessage();

		emailService.sendContactEmail(toEmail, subject, body);

		return "redirect:/";
	}

}
