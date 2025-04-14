package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.repository.BookRepository;
import com.onlinelibrary.steadyleafs.repository.MemberHomeRepository;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberHomeService {

	private final MemberHomeRepository memberHomeRepository;
	private final MemberRepository memberRepository;
	private final BookRepository bookRepository;

	public List<Book> getAllMyBooks() {
		return memberHomeRepository.findAll();
	}

	public void borrowBook(Book book, Member member) {
		Book bookToUpdate = bookRepository.findById(book.getId())
						.orElseThrow(() -> new RuntimeException("Book not found"));
		bookToUpdate.setBorrowedBy(member);
		bookToUpdate.setStatus("BORROWED");

		member.getBorrowedBooks().add(bookToUpdate);

		bookRepository.save(bookToUpdate);
		memberRepository.save(member);
	}

	public Book getMyBookById(Integer id) {
		Book book = memberHomeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Book with id " + id + " does not exists in your List"));
		return book;
	}

	public void returnMyBook(Integer id) {
		getMyBookById(id);
		memberHomeRepository.deleteById(id);
	}

	public Member getMemberWithBorrowedBooks(int memberId) {
		return memberRepository.findByIdWithBorrowedBooks(memberId)
				.orElseThrow(() -> new RuntimeException("Member not found"));
	}

	public Member getCurrentMember(int memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new RuntimeException("Member not found"));
	}

}
