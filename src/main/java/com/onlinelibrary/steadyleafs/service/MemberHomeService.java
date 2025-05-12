package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.dto.BookReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.SignedInMemberDto;
import com.onlinelibrary.steadyleafs.repository.BookRepository;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberHomeService {

	private final MemberRepository memberRepository;
	private final BookRepository bookRepository;
	private final BookService bookService;

	public void borrowBook(BookReturnDto book, SignedInMemberDto signedInMemberDto) {
		Book bookFromDatabase = bookService.getBookById(book.getId());

		Member member = memberRepository.findById(signedInMemberDto.getId())
				.orElseThrow(() -> new RuntimeException("Signed in member not found."));

		bookFromDatabase.setBorrowedBy(member);

		bookRepository.save(bookFromDatabase);
		memberRepository.save(member);
	}

	public void returnMyBook(Integer id) {
		Book bookToReturn = bookService.getBookById(id);
		Member member = bookToReturn.getBorrowedBy();

		bookToReturn.getBorrowedBy().getBorrowedBooks().remove(bookToReturn);

		bookToReturn.setBorrowedBy(null);

		memberRepository.save(member);
		bookRepository.save(bookToReturn);
	}

	public SignedInMemberDto getMemberWithBorrowedBooks(int memberId) {
		Member member = memberRepository.findByIdWithBorrowedBooks(memberId)
				.orElseThrow(() -> new RuntimeException("Member not found"));

		List<Book> borrowedBooks = member.getBorrowedBooks();
		borrowedBooks.sort(Comparator.comparing(Book::getTitle));

		SignedInMemberDto signedInMemberDto = new SignedInMemberDto();

		return signedInMemberDto.mapFromMember(member);
	}

}
