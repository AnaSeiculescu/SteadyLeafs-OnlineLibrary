package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;

	private User user = new User();
	private Member member = new Member();

	public User mapToUser(PasswordEncoder passwordEncoder) {
		user.setEmail(this.email);
		user.setPassword(passwordEncoder.encode(this.password));
		user.setRole("ROLE_MEMBER");
		return user;
	}

	public Member mapToMember() {
		member.setFirstName(this.firstName);
		member.setLastName(this.lastName);
		member.setUser(this.user);
		member.setBorrowedBooks(new ArrayList<>());
		return member;
	}
}
