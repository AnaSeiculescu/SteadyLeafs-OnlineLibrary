package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

	@NotBlank(message = "Title is required")
	@Pattern(regexp = "^[A-Za-z0-9 .,&'!?-]{1,100}$", message = "Title must use valid characters")
	private String title;

	@NotBlank(message = "Author is required")
	@Pattern(regexp = "^[A-Za-z .,&'-]{1,100}$", message = "Author must use valid characters")
	private String author;

	private String coverUrl;
	private String status;
	private Integer borrowedById;

	public Book mapToBook(Book book) {
		book.setTitle(this.title);
		book.setAuthor(this.author);

		return book;
	}

	public static BookUpdateDto mapFromBook(Book book) {
		BookUpdateDto bookUpdateDto = new BookUpdateDto();
		bookUpdateDto.setId(book.getId());
		bookUpdateDto.setTitle(book.getTitle());
		bookUpdateDto.setAuthor(book.getAuthor());
		bookUpdateDto.setCoverUrl(book.getCoverUrl());
		bookUpdateDto.setStatus(book.getStatus());

		if (book.getBorrowedBy() != null) {
			bookUpdateDto.setBorrowedById(book.getBorrowedBy().getId());
		} else {
			bookUpdateDto.setBorrowedById(null);
		}

		return bookUpdateDto;
	}

}
