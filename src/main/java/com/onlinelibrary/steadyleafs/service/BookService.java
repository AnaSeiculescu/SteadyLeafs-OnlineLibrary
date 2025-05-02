package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.BookReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.BookUpdateDto;
import com.onlinelibrary.steadyleafs.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {
	private final BookRepository bookRepository;

	public List<BookReturnDto> getAllBooks() {
		List<Book> booksFromDatabase =  bookRepository.findAll();
		return booksFromDatabase.stream()
				.map(book -> new BookReturnDto().mapFromBook(book))
				.toList();
	}

	public List<Book> getAvailableBooks() {
		return bookRepository.findByBorrowedByIsNull();
	}

	public List<Book> getLoanedBooks() {
		return bookRepository.findByBorrowedByIsNotNull();
	}

	public void createBook(Book book) {
		bookRepository.save(book);
	}

	public BookUpdateDto updateBook(BookUpdateDto bookUpdateDto) {
		Book bookFromDatabase = bookRepository.findById(bookUpdateDto.getId())
				.orElseThrow(() -> new RuntimeException("Book with id " + bookUpdateDto.getId() + " does not exists"));

		bookRepository.save(bookUpdateDto.mapToBook(bookFromDatabase));

		return BookUpdateDto.mapFromBook(bookFromDatabase);
	}

	public BookReturnDto getBookById(Integer id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Book with id " + id + " does not exists"));
		return new BookReturnDto().mapFromBook(book);
	}

	public void deleteBook(Integer id) {
		getBookById(id);
		bookRepository.deleteById(id);
	}

	public List<Book> getBooksByBorrowedBy(Integer memberId) {
		List<Book> borrowedBooks = bookRepository.findByBorrowedBy_Id(memberId);
		return borrowedBooks;
	}
}
