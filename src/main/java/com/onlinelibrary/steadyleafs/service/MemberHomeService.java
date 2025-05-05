package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.dto.BookReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.SignedInMemberDto;
import com.onlinelibrary.steadyleafs.repository.BookRepository;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberHomeService {

	private final MemberRepository memberRepository;
	private final BookRepository bookRepository;

	public void borrowBook(BookReturnDto book, SignedInMemberDto signedInMemberDto) {
		Book bookFromDatabase = getMyBookById(book.getId());

		Member member = memberRepository.findById(signedInMemberDto.getId())
				.orElseThrow(() -> new RuntimeException("Signed in member not found."));

		bookFromDatabase.setBorrowedBy(member);
//		bookFromDatabase.setStatus("BORROWED");

		bookRepository.save(bookFromDatabase);
		memberRepository.save(member);
	}

	public Book getMyBookById(Integer id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Book with id " + id + " does not exists in your List"));
		return book;
	}

	public void returnMyBook(Integer id) {
		Book bookToReturn = getMyBookById(id);
		Member member = bookToReturn.getBorrowedBy();

		bookToReturn.getBorrowedBy().getBorrowedBooks().remove(bookToReturn);

		bookToReturn.setBorrowedBy(null);
//		bookToReturn.setStatus("AVAILABLE");

		memberRepository.save(member);
		bookRepository.save(bookToReturn);
	}

	public SignedInMemberDto getMemberWithBorrowedBooks(int memberId) {
		Member member = memberRepository.findByIdWithBorrowedBooks(memberId)
				.orElseThrow(() -> new RuntimeException("Member not found"));

		SignedInMemberDto signedInMemberDto = new SignedInMemberDto();

		return signedInMemberDto.mapFromMember(member);
	}

}
