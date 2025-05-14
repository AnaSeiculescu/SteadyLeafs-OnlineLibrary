package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
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
public class SignedInMemberDto {

	private int id;
	private String firstName;
	private String lastName;
	private User user;
	private List<BookReturnDto> borrowedBooks = new ArrayList<>();

	public SignedInMemberDto mapFromMember(Member member) {
		SignedInMemberDto signedInMemberDto = new SignedInMemberDto();
		signedInMemberDto.setId(member.getId());
		signedInMemberDto.setFirstName(member.getFirstName());
		signedInMemberDto.setLastName(member.getLastName());
		signedInMemberDto.setUser(member.getUser());
		signedInMemberDto.setBorrowedBooks(member.getBorrowedBooks().stream()
				.map(book -> new BookReturnDto().mapFromBook(book))
				.toList());

		return signedInMemberDto;
	}
}
