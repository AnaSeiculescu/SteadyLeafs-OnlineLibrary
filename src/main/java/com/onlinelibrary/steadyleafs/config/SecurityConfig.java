package com.onlinelibrary.steadyleafs.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomAuthenticationSuccessHandler customSuccessHandler;

	@Bean
	public PasswordEncoder delegatingPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(c -> c.disable())
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.failureHandler(new CustomAuthenticationFailureHandler())
						.successHandler(customSuccessHandler)
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.deleteCookies("JSESSIONID")
				)
				.authorizeHttpRequests(authorize -> authorize
								.requestMatchers("/").permitAll()
								.requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/webjars/**").permitAll()
								.requestMatchers("/login-member", "/login-librarian", "/register/**", "/email/contact", "/email/send").permitAll()
								.requestMatchers("/adminHome/**").hasRole("ADMIN")
								.requestMatchers("/librarianHome/**").hasAnyRole("LIBRARIAN", "ADMIN")
								.requestMatchers("/memberHome/**").hasRole("MEMBER")
								.requestMatchers("/librarians/**").hasRole("ADMIN")
								.anyRequest().authenticated()
				);
		return http.build();
	}
}
