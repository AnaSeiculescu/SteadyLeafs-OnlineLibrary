package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.repository.MemberHomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberHomeService {

	private final MemberHomeRepository memberHomeRepository;

	public List<Member> getAllMyBooks() {
		return memberHomeRepository.findAll();
	}

	public void borrowBook(Book book) {
		Book bookToBorrow = new Book();
		bookToBorrow.setId(book.getId());
		memberHomeRepository.save(bookToBorrow);
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

}
