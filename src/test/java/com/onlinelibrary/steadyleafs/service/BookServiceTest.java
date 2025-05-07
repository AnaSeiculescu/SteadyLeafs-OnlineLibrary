package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.dto.BookCreateDto;
import com.onlinelibrary.steadyleafs.repository.BookRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookServiceTest {

	@InjectMocks
	@Autowired
	BookService bookService;

	@Mock
	BookRepository bookRepository;

	@Mock
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
		bookCreateDto.setCoverUrl("http://localhost");
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


}