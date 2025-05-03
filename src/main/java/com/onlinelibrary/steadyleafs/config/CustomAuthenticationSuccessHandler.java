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

		String intendedRole = request.getParameter("intendedRole");
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		boolean isLibrarian = false;
		boolean isMember = false;

		for (GrantedAuthority authority : authorities) {
			String role = authority.getAuthority();
			if (role.equals("ROLE_LIBRARIAN")) {
				isLibrarian = true;
			} else if (role.equals("ROLE_MEMBER")) {
				isMember = true;
			}
		}

		if ("LIBRARIAN".equals(intendedRole) && !isLibrarian) {
			response.sendRedirect("/login-librarian?error=accessDenied");
			return;
		} else if ("MEMBER".equals(intendedRole) && !isMember) {
			response.sendRedirect("/login-member?error=accessDenied");
			return;
		}

		String redirectURL = "/";
		if (isLibrarian) {
			redirectURL = "/librarianHome";
		} else if (isMember) {
			redirectURL = "/memberHome";
		}

		response.sendRedirect(redirectURL);

	}
}
