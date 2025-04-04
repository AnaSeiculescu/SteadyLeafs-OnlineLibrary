package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {
	private final BookRepository bookRepository;

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public void createBook(Book book) {
		bookRepository.save(book);
	}

	public Book updateBook(Book book) {
		Book bookToUpdate = bookRepository.findById(book.getId())
				.orElseThrow(() -> new RuntimeException("Book with id " + book.getId() + " does not exists"));

		return bookRepository.save(bookToUpdate.mapToUpdate(book));
	}

	public Book getBookById(Integer id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Book with id " + id + " does not exists"));
		return book;
	}

	public void deleteBook(Integer id) {
		getBookById(id);
		bookRepository.deleteById(id);

	}
}
