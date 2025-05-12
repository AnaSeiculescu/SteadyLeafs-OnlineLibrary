package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.dto.BookReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.SignedInMemberDto;
import com.onlinelibrary.steadyleafs.repository.BookRepository;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class MemberHomeServiceTest {

	@MockitoBean
	MemberRepository memberRepository;

	@MockitoBean
	private BookRepository bookRepository;

	@MockitoBean
	BookService bookService;

	@InjectMocks
	@Autowired
	MemberHomeService memberHomeService;

	@Test
	void borrowBookGivenOneBookAndOneSignedInMemberExpectBookToBeBorrowedByMember() {
		BookReturnDto bookReturnDto = new BookReturnDto();
		bookReturnDto.setId(3);

		SignedInMemberDto memberSignedInDto = new SignedInMemberDto();
		memberSignedInDto.setId(7);

		Book book = new Book();
		Member member = new Member();

		when(bookService.getBookById(3))
				.thenReturn(book);
		when(memberRepository.findById(7))
				.thenReturn(Optional.of(member));

		memberHomeService.borrowBook(bookReturnDto, memberSignedInDto);

		assertEquals(member, book.getBorrowedBy());
		verify(bookRepository).save(book);
		verify(memberRepository).save(member);
	}

	@Test
	void borrowBookWhenMemberNotFoundExpectException() {
		BookReturnDto bookReturnDto = new BookReturnDto();
		bookReturnDto.setId(3);

		SignedInMemberDto memberSignedInDto = new SignedInMemberDto();
		memberSignedInDto.setId(7);

		Book book = new Book();

		when(bookService.getBookById(3))
				.thenReturn(book);
		when(memberRepository.findById(7))
				.thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> memberHomeService.borrowBook(bookReturnDto, memberSignedInDto));

		assertEquals("Signed in member not found", exception.getMessage());
	}

	@Test
	void returnMyBookExpectBookToBeRemovedFromMemberBookList() {
		Book book = new Book();
		book.setId(3);

		Member member = new Member();

		book.setBorrowedBy(member);
		member.setBorrowedBooks(new ArrayList<>(List.of(book)));

		when(bookService.getBookById(3))
				.thenReturn(book);

		memberHomeService.returnMyBook(3);

		assertEquals(null, book.getBorrowedBy());
		assertFalse(member.getBorrowedBooks().contains(book));
		verify(bookRepository).save(book);
		verify(memberRepository).save(member);
	}

	@Test
	void returnBookWhenBorrowerNotFound() {
		Book book = new Book();
		book.setId(3);

		when(bookService.getBookById(3))
				.thenReturn(book);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> memberHomeService.returnMyBook(3));

		assertEquals("Borrower not found", exception.getMessage());
	}
}