package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReturnDto {

	private int id;
	private String email;
	private String password;
	private String role;
	private Member member;

	public UserReturnDto mapFromUser(User user) {
		UserReturnDto userReturnDto = new UserReturnDto();
		userReturnDto.setId(user.getId());
		userReturnDto.setEmail(user.getEmail());
		userReturnDto.setPassword(user.getPassword());
		userReturnDto.setRole(user.getRole());
		userReturnDto.setMember(user.getMember());

		return userReturnDto;
	}
}
