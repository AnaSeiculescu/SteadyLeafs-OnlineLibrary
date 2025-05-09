package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.dto.BookCreateDto;
import com.onlinelibrary.steadyleafs.model.dto.BookReturnDto;
import com.onlinelibrary.steadyleafs.repository.BookRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
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

//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		Validator validator = factory.getValidator();
//
//		var violations = validator.validate(invalidBookDto);
//		assertFalse(violations.isEmpty());
//
//		var violation = violations.iterator().next();
//		assertEquals("Title must use valid characters", violation.getMessage());

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
//		bookCreateDto.setCoverUrl("http://localhost");
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
}