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
	private int userId;
	private List<Book> borrowedBooks;

	public MemberReturnDto mapFromMember(Member member) {
		MemberReturnDto memberReturnDto = new MemberReturnDto();
		memberReturnDto.setId(member.getId());
		memberReturnDto.setFirstName(member.getFirstName());
		memberReturnDto.setLastName(member.getLastName());
		memberReturnDto.setUserId(member.getUser().getId());
		memberReturnDto.setBorrowedBooks(new ArrayList<>(member.getBorrowedBooks()));

		return memberReturnDto;
	}

}
