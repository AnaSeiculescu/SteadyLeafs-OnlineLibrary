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
public class BookCreateDto {

	@NotBlank(message = "Title is required")
	@Pattern(regexp = "^[A-Za-z0-9 .,'!?-]{1,100}$", message = "Title must use valid characters")
	private String title;

	@NotBlank(message = "Author is required")
	@Pattern(regexp = "^[A-Za-z .,'-]{1,100}$", message = "Author must use valid characters")
	private String author;

	private String coverUrl;

	@Pattern(regexp = "available", message = "Status must be 'available'")
	private String status = "available";

	public Book mapToBook(BookCreateDto bookCreateDto) {
		Book book = new Book();
		book.setTitle(bookCreateDto.getTitle());
		book.setAuthor(bookCreateDto.getAuthor());
		book.setCoverUrl(bookCreateDto.getCoverUrl());
		book.setStatus(this.status);

		return book;
	}

}
