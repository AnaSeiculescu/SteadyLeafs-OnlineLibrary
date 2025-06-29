package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.dto.BookCreateDto;
import com.onlinelibrary.steadyleafs.model.dto.BookReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.BookUpdateDto;
import com.onlinelibrary.steadyleafs.repository.BookRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class BookService {
	private final BookRepository bookRepository;
	private final BookCoverApiService bookCoverApiService;

	public List<BookReturnDto> getAllBooks() {
		List<Book> booksFromDatabase =  bookRepository.findAll(Sort.by("title").ascending());
		return booksFromDatabase.stream()
				.map(book -> new BookReturnDto().mapFromBook(book))
				.toList();
	}

	public List<BookReturnDto> getAvailableBooks() {
		List<Book> availableBooks = bookRepository.findByBorrowedByIsNull(Sort.by("title").ascending());
		return availableBooks.stream()
				.map(book -> new BookReturnDto().mapFromBook(book))
				.toList();
	}

	public List<BookReturnDto> getLoanedBooks() {
		List<Book> loanedBooks = bookRepository.findByBorrowedByIsNotNull(Sort.by("title").ascending());
		return loanedBooks.stream()
				.map(book -> new BookReturnDto().mapFromBook(book))
				.toList();
	}

	public Book createBook(@Valid BookCreateDto bookCreateDto) {
		if (bookCreateDto == null) {
			throw new RuntimeException("Missing book data");
		}

		Book book = bookCreateDto.mapToBook(bookCreateDto);
		String bookCoverUrl = bookCoverApiService.getCoverUrl(bookCreateDto.getTitle());
		book.setCoverUrl(bookCoverUrl);
		return bookRepository.save(book);
	}

	public BookUpdateDto updateBook(BookUpdateDto bookUpdateDto) {
		Book bookFromDatabase = bookRepository.findById(bookUpdateDto.getId())
				.orElseThrow(() -> new RuntimeException("Book with id " + bookUpdateDto.getId() + " does not exists"));

		if (!bookFromDatabase.getTitle().equals(bookUpdateDto.getTitle())) {
			String bookCoverUrl = bookCoverApiService.getCoverUrl(bookUpdateDto.getTitle());
			bookFromDatabase.setCoverUrl(bookCoverUrl);
		}
		bookRepository.save(bookUpdateDto.mapToBook(bookFromDatabase));

		return bookUpdateDto;
	}

	public Book getBookById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}
		if (id < 0) {
			throw new IllegalArgumentException("ID cannot be negative");
		}

		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Book with id " + id + " does not exists"));
		return book;
	}

	public void deleteBook(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}
		if (id < 0) {
			throw new IllegalArgumentException("ID cannot be negative");
		}

		getBookById(id);
		bookRepository.deleteById(id);
	}

	public List<Book> getBooksByBorrowedBy(Integer memberId) {
		List<Book> borrowedBooks = bookRepository.findByBorrowedBy_Id(memberId);
		return borrowedBooks;
	}

	public int getNumberOfBooks() {
		return getAllBooks().size();
	}

	public int getNumberOfLoanedBooks() {
		return getLoanedBooks().size();
	}

	public int getNumberOfAvailableBooks() {
		return getAvailableBooks().size();
	}

	public List<BookReturnDto> getBookByTitleOrAuthor(String searchString) {
		List<Book> booksByTitleOrAuthor = bookRepository.searchByTitleOrAuthor(searchString);
		return booksByTitleOrAuthor.stream()
				.map(book -> new BookReturnDto().mapFromBook(book))
				.toList();
	}

	public Page<BookReturnDto> getAllBooks(Pageable pageable) {
		Page<Book> bookPageFromDb = bookRepository.findAll(pageable);
		return bookPageFromDb.map(book -> new BookReturnDto().mapFromBook(book));
	}

	public Page<BookReturnDto> getAvailableBooks(Pageable pageable) {
		Page<Book> availableBookPageFromDb = bookRepository.findByBorrowedByIsNull(pageable);
		return availableBookPageFromDb.map(book -> new BookReturnDto().mapFromBook(book));
	}

	public Page<BookReturnDto> getLoanedBooks(Pageable pageable) {
		Page<Book> loanedBookPageFromDb = bookRepository.findByBorrowedByIsNotNull(pageable);
		return loanedBookPageFromDb.map(book -> new BookReturnDto().mapFromBook(book));
	}
}
