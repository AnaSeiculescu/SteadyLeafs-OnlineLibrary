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

	public MemberUpdateDto(int id, String firstName, String lastName, int userId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userId = userId;
	}

	public Member mapToMember(Member member) {
		member.setFirstName(this.firstName);
		member.setLastName(this.lastName);
		return member;
	}

	public static MemberUpdateDto mapFromMember(Member member) {
		MemberUpdateDto memberUpdateDto = new MemberUpdateDto(member.getId(), member.getFirstName(), member.getLastName(), member.getUser().getId());
		return memberUpdateDto;
	}
}
