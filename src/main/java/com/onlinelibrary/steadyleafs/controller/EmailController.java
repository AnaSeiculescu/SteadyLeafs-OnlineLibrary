package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.dto.ContactFormDto;
import com.onlinelibrary.steadyleafs.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/sendEmail")
	public String sendEmail(@RequestBody ContactFormDto contactFormdto) {
		String toEmail = "anaseiculescu@gmail.com";
		String subject = contactFormdto.getSubject();
		String body = "Name " + contactFormdto.getName() + "\n"
					+ "Email " + contactFormdto.getEmail() + "\n"
					+ "Message " + contactFormdto.getMessage();

		emailService.sendContactEmail(toEmail, subject, body);

		return "Email sent successfully!";
	}

}
