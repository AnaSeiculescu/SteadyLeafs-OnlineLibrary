package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.BookCreateDto;
import com.onlinelibrary.steadyleafs.model.dto.BookReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.BookUpdateDto;
import com.onlinelibrary.steadyleafs.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {

	@InjectMocks
	@Autowired
	BookService bookService;

	@MockitoBean
	BookRepository bookRepository;

	@MockitoBean
	BookCoverApiService bookCoverApiService;

	@Test
	public void createBookWhenNoDataExpectException() {
		RuntimeException exception = assertThrows(RuntimeException.class, () -> bookService.createBook(null));
		assertEquals("Missing book data", exception.getMessage());
	}

	@Test
	void createBookGivenInvalidInputExpectException() {

		BookCreateDto invalidBookDto = new BookCreateDto();
		invalidBookDto.setTitle("Cinderella$#");
		invalidBookDto.setAuthor("Perrault");
		invalidBookDto.setCoverUrl("http://localhost");
		invalidBookDto.setStatus("available");

		when(bookRepository.save(any()))
				.thenReturn(new BookCreateDto().mapToBook(invalidBookDto));

//		assertThrows(ConstraintViolationException.class, () -> {
//			bookService.createBook(invalidBookDto);
//		});

		RuntimeException exception = assertThrows(RuntimeException.class, () -> bookService.createBook(invalidBookDto));
		assertEquals("createBook.bookCreateDto.title: Title must use valid characters", exception.getMessage());

	}

	@Test
	void createBookGivenOneBookExpectBookCreated() {

		BookCreateDto bookCreateDto = new BookCreateDto();
		bookCreateDto.setTitle("Cenusareasa");
		bookCreateDto.setAuthor("Perrault");
		bookCreateDto.setStatus("available");

		String mockedCoverUrl = "http://mocked-cover-url.com";

		when(bookCoverApiService.getCoverUrl("Cenusareasa"))
				.thenReturn(mockedCoverUrl);
		when(bookRepository.save(any()))
				.thenReturn(new BookCreateDto().mapToBook(bookCreateDto));

		Book createdBook = bookService.createBook(bookCreateDto);

		Book expectedBook = new Book();
		expectedBook.setTitle("Cenusareasa");
		expectedBook.setAuthor("Perrault");
		expectedBook.setCoverUrl(mockedCoverUrl);
		expectedBook.setStatus("available");

		assertEquals(expectedBook, createdBook);
	}

	@Test
	void getAllBooksCreateTwoBooksExpectTwoBooksFromRepo() {

		Book book1 = new Book();
		book1.setTitle("Cenusareasa");
		book1.setAuthor("Perrault");
		String mockedCoverUrl1 = "http://mocked-cover-url1.com";

		Book book2 = new Book();
		book2.setTitle("Rapunzel");
		book2.setAuthor("Grimm");
		String mockedCoverUrl2 = "http://mocked-cover-url2.com";

		List<Book> mockedBooks = List.of(book1, book2);

		when(bookRepository.findAll(Sort.by("title").ascending()))
				.thenReturn(mockedBooks);

		List<BookReturnDto> result = bookService.getAllBooks();

		assertEquals(2, result.size());
		assertEquals("Rapunzel", result.get(1).getTitle());
		assertEquals("Perrault", result.get(0).getAuthor());
	}

	@Test
	void getAllBooksWhenNoInputExpectEmptyList() {
		when(bookRepository.findAll(Sort.by("title").ascending()))
				.thenReturn(List.of());

		List<BookReturnDto> result = bookService.getAllBooks();

//		assertTrue(result.isEmpty());
		assertEquals(0, result.size());
	}

	@Test
	void getAllBooksBooksShouldBeSortedByTitle() {
		Book book1 = new Book();
		book1.setTitle("Rapunzel");

		Book book2 = new Book();
		book2.setTitle("Cenusareasa");

		when(bookRepository.findAll(Sort.by("title").ascending()))
				.thenReturn(List.of(book2, book1));

		List<BookReturnDto> result = bookService.getAllBooks();

		assertEquals("Cenusareasa", result.get(0).getTitle());
		assertEquals("Rapunzel", result.get(1).getTitle());

	}

	@Test
	void getAllBooksCheckIfDtoMapIsCorrect() {
		Book book = new Book();
		book.setId(1);
		book.setTitle("Cenusareasa");
		book.setAuthor("Perrault");
		book.setCoverUrl("http://mocked-cover-url.com");
		book.setStatus("available");

		when(bookRepository.findAll(Sort.by("title").ascending()))
				.thenReturn(List.of(book));
		List<BookReturnDto> result = bookService.getAllBooks();

		BookReturnDto bookReturnDto = result.get(0);

		assertEquals(1, result.size());
		assertEquals("Cenusareasa", bookReturnDto.getTitle());
		assertEquals("Perrault", bookReturnDto.getAuthor());
		assertEquals("http://mocked-cover-url.com", bookReturnDto.getCoverUrl());
		assertEquals("available", bookReturnDto.getStatus());
	}

	@Test
	void getAvailableBooksReturnsMappedDtoList() {
		Book book1 = new Book();
		book1.setTitle("Cenusareasa");
		book1.setAuthor("Perrault");
		book1.setCoverUrl("http://mocked-cover-url1.com");
		book1.setStatus("available");

		Book book2 = new Book();
		book2.setTitle("Rapunzel");
		book2.setAuthor("Grimm");
		book2.setCoverUrl("http://mocked-cover-url2.com");
		book2.setStatus("available");

		List<Book> mockedBooks = List.of(book1, book2);

		when(bookRepository.findByBorrowedByIsNull(Sort.by("title").ascending()))
				.thenReturn(mockedBooks);

		List<BookReturnDto> result = bookService.getAvailableBooks();

		assertEquals(2, result.size());

		BookReturnDto dto1 = result.get(0);
		assertEquals("Cenusareasa", dto1.getTitle());
		assertEquals("Perrault", dto1.getAuthor());
		assertEquals("http://mocked-cover-url1.com", dto1.getCoverUrl());
		assertEquals("available", dto1.getStatus());

		BookReturnDto dto2 = result.get(1);
		assertEquals("Rapunzel", dto2.getTitle());
		assertEquals("Grimm", dto2.getAuthor());
		assertEquals("http://mocked-cover-url2.com", dto2.getCoverUrl());
		assertEquals("available", dto2.getStatus());
	}

	@Test
	void getLoanedBooksReturnsMappedDtoList() {
		User user1 = new User();
		user1.setId(1);
		user1.setEmail("user1@email.com");

		User user2 = new User();
		user2.setId(2);
		user2.setEmail("user2@email.com");

		Member member1 = new Member();
		member1.setUser(user1);

		Member member2 = new Member();
		member2.setUser(user2);

		Book book1 = new Book();
		book1.setTitle("Cenusareasa");
		book1.setAuthor("Perrault");
		book1.setCoverUrl("http://mocked-cover-url1.com");
		book1.setStatus("BORROWED");
		book1.setBorrowedBy(member1);

		Book book2 = new Book();
		book2.setTitle("Rapunzel");
		book2.setAuthor("Grimm");
		book2.setCoverUrl("http://mocked-cover-url2.com");
		book2.setStatus("BORROWED");
		book2.setBorrowedBy(member2);

		List<Book> mockedBooks = List.of(book1, book2);

		when(bookRepository.findByBorrowedByIsNotNull(Sort.by("title").ascending()))
				.thenReturn(mockedBooks);

		List<BookReturnDto> result = bookService.getLoanedBooks();

		assertEquals(2, result.size());
		assertEquals("Cenusareasa", result.get(0).getTitle());
		assertEquals("Rapunzel", result.get(1).getTitle());
	}

	@Test
	void getBookByIdWhenBookExistsReturnsMappedDto() {
		Book book1 = new Book();
		book1.setId(1);
		book1.setTitle("Rapunzel");
		book1.setAuthor("Grimm");
		book1.setCoverUrl("http://mocked-cover-url.com");
		book1.setStatus("available");

		when(bookRepository.findById(1))
				.thenReturn(Optional.of(book1));

		BookReturnDto bookReturnDto = bookService.getBookById(1);

		assertEquals("Rapunzel", bookReturnDto.getTitle());
		assertEquals("Grimm", bookReturnDto.getAuthor());
		assertEquals("http://mocked-cover-url.com", bookReturnDto.getCoverUrl());
		assertEquals("available", bookReturnDto.getStatus());
	}

	@Test
	void getBookByIdWhenBookDoesNotExistsExpectException() {
		when(bookRepository.findById(95))
				.thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> bookService.getBookById(95));
		assertEquals("Book with id 95 does not exists", exception.getMessage());
	}

	@Test
	void getBookByIdWhenNullInputExpectException() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bookService.getBookById(null));
		assertEquals("ID cannot be null", exception.getMessage());

		verify(bookRepository, never()).findById(any());
	}

	@Test
	void getBookByIdWhenNegativeInputExpectException() {
		when(bookRepository.findById(-1)).thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bookService.getBookById(-1));
		assertEquals("ID cannot be negative", exception.getMessage());

		verify(bookRepository, never()).findById(-1);
	}

	@Test
	void updateBookAuthorChangesShouldSaveNewAuthor_TitleAndCoverStaysTheSame() {
		BookUpdateDto bookUpdateDto = new BookUpdateDto();
		bookUpdateDto.setId(1);
		bookUpdateDto.setTitle("Rapunzel");
		bookUpdateDto.setAuthor("Grimm");

		Book bookFromDatabase = new Book();
		bookFromDatabase.setId(1);
		bookFromDatabase.setTitle("Rapunzel");
		bookFromDatabase.setAuthor("Old Title");

		when(bookRepository.findById(1))
				.thenReturn(Optional.of(bookFromDatabase));
		when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

		BookUpdateDto result = bookService.updateBook(bookUpdateDto);

		verify(bookCoverApiService, never()).getCoverUrl(anyString());
		verify(bookRepository).save(argThat(savedBook ->
				savedBook.getAuthor().equals("Grimm") &&
				savedBook.getTitle().equals("Rapunzel")
		));
	}

	@Test
	void updateBookWhenTitleChangesCoverShouldChangeToo() {
		BookUpdateDto bookUpdateDto = new BookUpdateDto();
		bookUpdateDto.setId(1);
		bookUpdateDto.setTitle("New Title");

		Book bookFromDatabase = new Book();
		bookFromDatabase.setId(1);
		bookFromDatabase.setTitle("Rapunzel");

		when(bookRepository.findById(1))
				.thenReturn(Optional.of(bookFromDatabase));
		when(bookCoverApiService.getCoverUrl("New Title")).thenReturn("http://new-cover-url.com");

		BookUpdateDto result = bookService.updateBook(bookUpdateDto);

		Book savedBook = new Book();
		savedBook.setId(1);
		savedBook.setTitle("New Title");
		savedBook.setCoverUrl("http://new-cover-url.com");

		when(bookRepository.save(any(Book.class)))
				.thenReturn(savedBook);

		assertEquals("New Title", result.getTitle());
		verify(bookCoverApiService).getCoverUrl("New Title");
		verify(bookRepository).save(any(Book.class));
	}

	@Test
	void updateBookWhenBookNotFoundExpectException() {
		BookUpdateDto bookUpdateDto = new BookUpdateDto();
		bookUpdateDto.setId(95);
		bookUpdateDto.setTitle("Rapunzel");

		when(bookRepository.findById(95))
				.thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> bookService.updateBook(bookUpdateDto));
		assertEquals("Book with id 95 does not exists", exception.getMessage());
	}

	@Test
	void deleteBookWhenBookExists() {
		Book book = new Book();
		book.setId(1);
		book.setTitle("Rapunzel");

		when(bookRepository.findById(1))
				.thenReturn(Optional.of(book));

		doNothing().when(bookRepository).deleteById(1);

		bookService.deleteBook(1);

		verify(bookRepository).deleteById(1);
	}

	@Test
	void deleteBookWhenBookDoesNotExistsThrowsException() {
		when(bookRepository.findById(95))
				.thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> bookService.deleteBook(95));
		assertEquals("Book with id 95 does not exists", exception.getMessage());

		verify(bookRepository, never()).deleteById(95);
	}

	@Test
	void deleteBookWhenNullInputExpectException() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bookService.deleteBook(null));
		assertEquals("ID cannot be null", exception.getMessage());

		verify(bookRepository, never()).deleteById(any());
	}

	@Test
	void deleteBookWhenNegativeInputExpectException() {
		when(bookRepository.findById(-1))
				.thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bookService.deleteBook(-1));
		assertEquals("ID cannot be negative", exception.getMessage());

		verify(bookRepository, never()).deleteById(-1);
	}

}