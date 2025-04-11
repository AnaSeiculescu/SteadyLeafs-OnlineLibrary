package com.onlinelibrary.steadyleafs.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
										HttpServletResponse response,
										org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {

		String intendedRole = request.getParameter("intendedRole");

		String redirectURL = "/login-member?error=true";
		if ("LIBRARIAN".equalsIgnoreCase(intendedRole)) {
			redirectURL = "/login-librarian?error=true";
		}

		response.sendRedirect(redirectURL);

	}
}
