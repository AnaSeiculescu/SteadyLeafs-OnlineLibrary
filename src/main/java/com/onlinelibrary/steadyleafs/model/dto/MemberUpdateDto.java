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
public class MemberUpdateDto {
	private int id;
	private String firstName;
	private String lastName;
	private int userId;
	private List<Book> borrowedBooks = new ArrayList<>();

//	public MemberUpdateDto(String firstName, String lastName) {
//		this.firstName = firstName;
//		this.lastName = lastName;
//	}

	public Member mapToMember(Member oldMember) {
		oldMember.setFirstName(this.firstName);
		oldMember.setLastName(this.lastName);

		return oldMember;
	}

//	public static MemberUpdateDto mapFromMember(Member member) {
//		MemberUpdateDto memberUpdateDto = new MemberUpdateDto(member.getId(), member.getFirstName(), member.getLastName(), member.getUser().getId());
//		return memberUpdateDto;
//	}

	public static MemberUpdateDto mapFromMember(Member member) {
		MemberUpdateDto memberUpdateDto = new MemberUpdateDto(member.getId(), member.getFirstName(), member.getLastName(), member.getUser().getId(), member.getBorrowedBooks());
		return memberUpdateDto;
	}
}
