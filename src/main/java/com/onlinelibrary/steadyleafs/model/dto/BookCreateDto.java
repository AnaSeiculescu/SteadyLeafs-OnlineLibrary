package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateDto {

	private int id;
	private String title;
	private String author;
	private String status;

	public Book mapToBook(BookCreateDto bookCreateDto) {
		Book book = new Book();
		book.setId(bookCreateDto.getId());
		book.setTitle(bookCreateDto.getTitle());
		book.setAuthor(bookCreateDto.getAuthor());
		book.setStatus(bookCreateDto.getStatus());

		return book;
	}

//	public BookCreateDto mapFromBook(Book book) {
//		BookCreateDto bookCreateDto = new BookCreateDto();
//		bookCreateDto.setId(book.getId());
//		bookCreateDto.setTitle(book.getTitle());
//		bookCreateDto.setAuthor(book.getAuthor());
//		bookCreateDto.setStatus(book.getStatus());
//		bookCreateDto.setBorrowedById(book.getBorrowedBy().getId());
//
//		return bookCreateDto;
//	}
}
