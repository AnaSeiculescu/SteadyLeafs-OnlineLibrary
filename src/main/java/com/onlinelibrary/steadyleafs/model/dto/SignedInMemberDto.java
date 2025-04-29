package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.Book;
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
	private List<Book> borrowedBooks = new ArrayList<>();

	public SignedInMemberDto mapFromMember(Member member) {
		SignedInMemberDto signedInMemberDto = new SignedInMemberDto();
		signedInMemberDto.setId(member.getId());
		signedInMemberDto.setFirstName(member.getFirstName());
		signedInMemberDto.setLastName(member.getLastName());
		signedInMemberDto.setUser(member.getUser());
		signedInMemberDto.setBorrowedBooks(member.getBorrowedBooks());

		return signedInMemberDto;
	}

	public static Member mapToMember(SignedInMemberDto signedInMemberDto) {
		Member member = new Member();
		member.setId(signedInMemberDto.getId());
		member.setFirstName(signedInMemberDto.getFirstName());
		member.setLastName(signedInMemberDto.getLastName());
		member.setUser(signedInMemberDto.getUser());
		member.setBorrowedBooks(signedInMemberDto.getBorrowedBooks());

		return member;
	}

//	public Member mapToMember(Member member) {
//		member.setId(this.id);
//		member.setFirstName(this.firstName);
//		member.setLastName(this.lastName);
//		member.setBorrowedBooks(this.borrowedBooks);
//
//		return member;
//	}
}
