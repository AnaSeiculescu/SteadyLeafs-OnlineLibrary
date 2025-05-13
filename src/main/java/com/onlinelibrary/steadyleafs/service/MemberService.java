package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Librarian;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.dto.MemberReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.MemberUpdateDto;
import com.onlinelibrary.steadyleafs.repository.BookRepository;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final BookRepository bookRepository;

	public List<MemberReturnDto> getAllMembers() {
		List<Member> membersFromDatabase = memberRepository.findAll();

		membersFromDatabase.forEach(member -> {
			member.getBorrowedBooks().sort(Comparator.comparing(Book::getTitle));
		});

		return membersFromDatabase.stream()
				.map(member -> new MemberReturnDto().mapFromMember(member))
				.toList();
	}

	public Member createMember(Member member) {
		if (member == null) {
			throw new RuntimeException("Missing member data");
		}

		return memberRepository.save(member);
	}

	public Member createMemberFromLibrarian(Librarian librarian) {
		Member member = Member.mapFromLibrarian(librarian);
		member.setUser(librarian.getUser());
		memberRepository.save(member);

		return member;
	}

	public MemberUpdateDto updateMember(MemberUpdateDto memberUpdateDto) {
		Member memberFromDatabase = memberRepository.findById(memberUpdateDto.getId())
				.orElseThrow(() -> new RuntimeException("Member with id " + memberUpdateDto.getId() + " does not exists"));

		memberRepository.save(memberUpdateDto.mapToMember(memberFromDatabase));

		return memberUpdateDto;
	}

	public MemberReturnDto getMemberById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}
		if (id < 0) {
			throw new IllegalArgumentException("ID cannot be negative");
		}

		Member member = memberRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Member with id " + id + " does not exists"));

		member.getBorrowedBooks().sort(Comparator.comparing(Book::getTitle));

		MemberReturnDto memberReturnDto = new MemberReturnDto();
		return memberReturnDto.mapFromMember(member);
	}

	public MemberReturnDto getMemberByUserId(Integer userId) {
		Member member = memberRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Member with user id: " + userId + " not found"));

		MemberReturnDto memberReturnDto = new MemberReturnDto();
		return memberReturnDto.mapFromMember(member);
	}

	public void deleteMember(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}
		if (id < 0) {
			throw new IllegalArgumentException("ID cannot be negative");
		}

		Member member = memberRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Member with id " + id + " does not exists"));

		if (member.getBorrowedBooks() != null) {
			for(Book book :member.getBorrowedBooks()) {
				book.setBorrowedBy(null);
				bookRepository.save(book);
			}
			member.getBorrowedBooks().clear();
			memberRepository.save(member);
		}

		memberRepository.deleteById(member.getId());
	}

}
