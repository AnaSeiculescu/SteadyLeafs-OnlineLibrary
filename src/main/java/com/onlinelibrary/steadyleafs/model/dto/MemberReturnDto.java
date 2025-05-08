package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberReturnDto {
	private int id;
	private String firstName;
	private String lastName;
	private Integer userId;
	private List<BookReturnDto> borrowedBooks;

	public MemberReturnDto mapFromMember(Member member) {
		MemberReturnDto memberReturnDto = new MemberReturnDto();
		memberReturnDto.setId(member.getId());
		memberReturnDto.setFirstName(member.getFirstName());
		memberReturnDto.setLastName(member.getLastName());
		memberReturnDto.setUserId(member.getUser().getId());
		memberReturnDto.setBorrowedBooks(member.getBorrowedBooks().stream()
				.map(book -> new BookReturnDto().mapFromBook(book))
				.toList());

		return memberReturnDto;
	}

	public MemberReturnDto mapFromMemberBasic(Member member) {
		MemberReturnDto memberReturnDto = new MemberReturnDto();
		memberReturnDto.setId(member.getId());
		memberReturnDto.setFirstName(member.getFirstName());
		memberReturnDto.setLastName(member.getLastName());
		memberReturnDto.setUserId(member.getUser().getId());

		return memberReturnDto;
	}

//	public Member mapToMember() {
//		Member member = new Member();
//		member.setFirstName(this.firstName);
//		member.setLastName(this.lastName);
//
//		return member;
//	}

}
