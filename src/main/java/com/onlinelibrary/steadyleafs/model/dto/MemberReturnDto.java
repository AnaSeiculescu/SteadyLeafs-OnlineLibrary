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
public class MemberReturnDto {
	private int id;
	private String firstName;
	private String lastName;
	private User user;
	private List<Book> borrowedBooks = new ArrayList<>();

	public MemberReturnDto mapFromMember(Member member) {
		MemberReturnDto memberReturnDto = new MemberReturnDto();
		memberReturnDto.setId(member.getId());
		memberReturnDto.setFirstName(member.getFirstName());
		memberReturnDto.setLastName(member.getLastName());
		memberReturnDto.setUser(member.getUser());
		memberReturnDto.setBorrowedBooks(member.getBorrowedBooks());

		return memberReturnDto;
	}

}
