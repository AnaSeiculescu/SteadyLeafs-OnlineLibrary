package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
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

	public void borrowBook(Book book, SignedInMemberDto signedInMemberDto) {
		Book bookToUpdate = bookRepository.findById(book.getId())
				.orElseThrow(() -> new RuntimeException("Book not found"));

		Member member = memberRepository.findById(signedInMemberDto.getId())
				.orElseThrow(() -> new RuntimeException("Signed in member not found."));

//		bookToUpdate.setBorrowedBy(SignedInMemberDto.mapToMember(signedInMemberDto));
//		bookToUpdate.setBorrowedBy(signedInMemberDto.mapFromMember(bookToUpdate.getBorrowedBy()));

		bookToUpdate.setBorrowedBy(member);
		bookToUpdate.setStatus("BORROWED");

		signedInMemberDto.getBorrowedBooks().add(bookToUpdate);
//		Member member = SignedInMemberDto.mapToMember(signedInMemberDto);


		bookRepository.save(bookToUpdate);
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
		bookToReturn.setStatus("available");

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
