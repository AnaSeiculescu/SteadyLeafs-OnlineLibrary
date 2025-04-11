package com.onlinelibrary.steadyleafs.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
										HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {

		System.out.println("🎉 User successfully authenticated: " + authentication.getName());
		String redirectURL = "/users";

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("ROLE_MEMBER")) {
				redirectURL = "/memberHome";
				break;
			} else if (authority.getAuthority().equals("ROLE_LIBRARIAN")) {
				redirectURL = "/librarians/members";
				break;
			}
		}

		response.sendRedirect(redirectURL);

	}
}
