package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {

	private int id;

	@NotBlank(message = "First name is required")
	@Pattern(regexp = "^[A-Za-z .,'-]{1,100}$", message = "First name must use valid characters")
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Pattern(regexp = "^[A-Za-z .,'-]{1,100}$", message = "Last name must use valid characters")
	private String lastName;

	private int userId;
	private List<Book> borrowedBooks = new ArrayList<>();

	public Member mapToMember(Member oldMember) {
		oldMember.setFirstName(this.firstName);
		oldMember.setLastName(this.lastName);

		return oldMember;
	}

	public static MemberUpdateDto mapFromMember(Member member) {
		MemberUpdateDto memberUpdateDto = new MemberUpdateDto(member.getId(), member.getFirstName(), member.getLastName(), member.getUser().getId(), member.getBorrowedBooks());
		return memberUpdateDto;
	}
}
