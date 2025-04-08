package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String password;

	public User mapToUser(PasswordEncoder passwordEncoder) {
		User user = new User();
		user.setEmail(this.email);
		user.setPassword(passwordEncoder.encode(this.password));
		user.setRole("ROLE_MEMBER");
		return user;
	}
}
