package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.User;
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
	private String email;
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
