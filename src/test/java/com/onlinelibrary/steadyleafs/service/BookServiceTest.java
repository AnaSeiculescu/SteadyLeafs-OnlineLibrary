package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.dto.BookCreateDto;
import com.onlinelibrary.steadyleafs.model.dto.BookReturnDto;
import com.onlinelibrary.steadyleafs.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
		assertEquals("Missing book data.", exception.getMessage());
	}

	@Test
	void createBookGivenInvalidInputShouldThrowException() {

		BookCreateDto invalidBookDto = new BookCreateDto();
		invalidBookDto.setTitle("Cinderella$#");
		invalidBookDto.setAuthor("Perrault");
		invalidBookDto.setCoverUrl("http://localhost");
		invalidBookDto.setStatus("available");

		when(bookRepository.save(any())).thenReturn(new BookCreateDto().mapToBook(invalidBookDto));

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

		when(bookCoverApiService.getCoverUrl("Cenusareasa")).thenReturn(mockedCoverUrl);
		when(bookRepository.save(any())).thenReturn(new BookCreateDto().mapToBook(bookCreateDto));

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
	void getAllBooksWhenNoInputExpectsEmptyList() {
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
		assertEquals("Cenusareasa", result.getFirst().getTitle());
		assertEquals("Perrault", result.getFirst().getAuthor());
		assertEquals("http://mocked-cover-url.com", result.getFirst().getCoverUrl());
		assertEquals("available", result.getFirst().getStatus());
	}

}