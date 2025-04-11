package com.onlinelibrary.steadyleafs.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
				.httpBasic(Customizer.withDefaults())
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.failureHandler(new CustomAuthenticationFailureHandler())
						.successHandler(customSuccessHandler)
						.permitAll())
				.authorizeHttpRequests(authorize -> authorize
								.requestMatchers("/").permitAll()
								.requestMatchers("/login-member", "/login-librarian").permitAll()
								.requestMatchers("/memberHome").permitAll()
								.requestMatchers("/users/**").permitAll()
								.requestMatchers("/librarians/**").permitAll()
//						.requestMatchers("/users/create").permitAll()
								.requestMatchers("/books/**").permitAll()
//						.requestMatchers("/users/delete").hasRole("ADMIN")
//						.requestMatchers("/users/update").hasRole("ADMIN")
								.anyRequest().authenticated()

				);
		return http.build();
	}
}
