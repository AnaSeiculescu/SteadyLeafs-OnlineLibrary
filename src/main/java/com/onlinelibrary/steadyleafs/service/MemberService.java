package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.dto.MemberReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.MemberUpdateDto;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

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
		return memberRepository.save(member);
	}

	public MemberUpdateDto updateMember(MemberUpdateDto memberUpdateDto) {
		Member memberFromDatabase = memberRepository.findById(memberUpdateDto.getId())
				.orElseThrow(() -> new RuntimeException("Member with id " + memberUpdateDto.getId() + " does not exists"));


		memberRepository.save(memberUpdateDto.mapToMember(memberFromDatabase));

		MemberUpdateDto updatedMember = MemberUpdateDto.mapFromMember(memberFromDatabase);

		return updatedMember;
	}

	public MemberReturnDto getMemberById(Integer id) {
		Member member = memberRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User with id " + id + " does not exists"));

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
		Member member = memberRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User with id " + id + " does not exists"));
		memberRepository.deleteById(member.getId());
	}

}
