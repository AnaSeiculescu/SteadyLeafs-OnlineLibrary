package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.dto.MemberReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.MemberUpdateDto;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public List<MemberReturnDto> getAllMembers() {
		List<Member> membersFromDatabase = memberRepository.findAll();
		return membersFromDatabase.stream()
				.map(member -> new MemberReturnDto().mapFromMember(member))
				.toList();
	}

//	public void createMember(Member member) {
//		memberRepository.save(member);
//	}

	public MemberUpdateDto updateMember(MemberUpdateDto memberUpdateDto) {
		Member memberToUpdate = memberRepository.findById(memberUpdateDto.getId())
				.orElseThrow(() -> new RuntimeException("Member with id " + memberUpdateDto.getId() + " does not exists"));

		Member updatedMember = memberUpdateDto.mapToMember(memberToUpdate);
		memberRepository.save(updatedMember);

		return MemberUpdateDto.mapFromMember(updatedMember);
	}

	public Member getMemberById(Integer id) {
		Member member = memberRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User with id " + id + " does not exists"));
		return member;
	}

	public Member getMemberByUserId(Integer userId) {
		Member member = memberRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Member with user id: " + userId + " not found"));

		return member;
	}

	public void deleteUser(Integer id) {
		getMemberById(id);
		memberRepository.deleteById(id);

	}

}
