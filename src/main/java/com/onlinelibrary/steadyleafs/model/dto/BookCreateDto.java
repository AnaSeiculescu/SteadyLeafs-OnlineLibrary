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
public class BookCreateDto {

	private int id;
	private String title;
	private String author;
	private String status = "available";

	public Book mapToBook(BookCreateDto bookCreateDto) {
		Book book = new Book();
		book.setId(bookCreateDto.getId());
		book.setTitle(bookCreateDto.getTitle());
		book.setAuthor(bookCreateDto.getAuthor());
		book.setStatus(this.status);

		return book;
	}

}
