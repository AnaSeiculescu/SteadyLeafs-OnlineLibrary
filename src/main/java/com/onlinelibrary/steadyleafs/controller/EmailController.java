package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.dto.ContactFormDto;
import com.onlinelibrary.steadyleafs.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;

	@GetMapping("/contact")
	public String getSendEmailForm(Model model) {
		model.addAttribute("form", new ContactFormDto());

		return "onlineLibrary/contact";
	}

	@PostMapping("/send")
	public String sendEmail(@ModelAttribute ContactFormDto form, Model model) {
		String toEmail = "anaseiculescu@gmail.com";
		String subject = form.getSubject();
		String body = "Name: " + form.getName() + "\n"
					+ "Email: " + form.getEmail() + "\n"
					+ "Message: " + form.getMessage();

		try {
			emailService.sendContactEmail(toEmail, subject, body);

			return "redirect:/?emailSuccess=true";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Oops! Failed to send the message. Please try again.");
			model.addAttribute("form", new ContactFormDto());

			return "onlineLibrary/contact";
		}

	}

}
