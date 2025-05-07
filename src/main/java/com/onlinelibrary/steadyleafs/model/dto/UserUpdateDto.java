package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

	private int id;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@Pattern(regexp = "ROLE_MEMBER|ROLE_LIBRARIAN", message = "Role must be 'ROLE_MEMBER' or 'ROLE_LIBRARIAN'")
	private String role;

	public User mapToUser(User user) {
		user.setEmail(this.email);
		user.setRole(this.role);
		return user;
	}


	public static UserUpdateDto mapFromUser(User user) {
		UserUpdateDto userUpdateDto = new UserUpdateDto(user.getId(), user.getEmail(), user.getRole());
		return userUpdateDto;
	}
}
