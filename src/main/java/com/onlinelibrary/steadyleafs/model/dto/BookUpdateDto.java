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
public class BookUpdateDto {
	private int id;
	private String title;
	private String author;
	private String status;
	private Integer borrowedById;

	public Book mapToBook(Book oldBook) {
		oldBook.setTitle(this.title);
		oldBook.setAuthor(this.author);

		return oldBook;
	}

	public static BookUpdateDto mapFromBook(Book book) {
		BookUpdateDto bookUpdateDto = new BookUpdateDto();
		bookUpdateDto.setId(book.getId());
		bookUpdateDto.setTitle(book.getTitle());
		bookUpdateDto.setAuthor(book.getAuthor());
		bookUpdateDto.setStatus(book.getStatus());

		if (book.getBorrowedBy() != null) {
			bookUpdateDto.setBorrowedById(book.getBorrowedBy().getId());
		} else {
			bookUpdateDto.setBorrowedById(null);
		}

		return bookUpdateDto;
	}
}
