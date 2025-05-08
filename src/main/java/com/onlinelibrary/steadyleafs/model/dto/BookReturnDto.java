package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookReturnDto {
	private int id;
	private String title;
	private String author;
	private String coverUrl;
	private String status;
	private Integer borrowedById;
	private MemberReturnDto memberReturnDto;

	public BookReturnDto mapFromBook(Book book) {
		BookReturnDto bookReturnDto = new BookReturnDto();
		bookReturnDto.setId(book.getId());
		bookReturnDto.setTitle(book.getTitle());
		bookReturnDto.setAuthor(book.getAuthor());
		bookReturnDto.setCoverUrl(book.getCoverUrl());
		bookReturnDto.setStatus(book.getStatus());

		if (book.getBorrowedBy() != null) {
			bookReturnDto.setBorrowedById(book.getBorrowedBy().getId());
			bookReturnDto.setMemberReturnDto(new MemberReturnDto().mapFromMemberBasic(book.getBorrowedBy()));
		} else {
			bookReturnDto.setBorrowedById(null);
			bookReturnDto.setMemberReturnDto(null);
		}

		return bookReturnDto;
	}
}
