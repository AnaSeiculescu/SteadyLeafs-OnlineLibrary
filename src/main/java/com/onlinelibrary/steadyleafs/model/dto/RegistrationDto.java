package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password is required")
	private String password;

	@NotBlank(message = "First name is required")
	@Pattern(regexp = "^[A-Za-z .,'-]{1,100}$", message = "First name must use valid characters")
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Pattern(regexp = "^[A-Za-z .,'-]{1,100}$", message = "Last name must use valid characters")
	private String lastName;

	public User mapToUser(PasswordEncoder passwordEncoder) {
		User user = new User();
		user.setEmail(this.email);
		user.setPassword(passwordEncoder.encode(this.password));
		user.setRole("ROLE_MEMBER");
		return user;
	}

	public Member mapToMember() {
		Member member = new Member();
		member.setFirstName(this.firstName);
		member.setLastName(this.lastName);
//		member.setUser(this.user);
		member.setBorrowedBooks(new ArrayList<>());
		return member;
	}
}
