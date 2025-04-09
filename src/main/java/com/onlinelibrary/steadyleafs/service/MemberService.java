package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.dto.MemberReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.MemberUpdateDto;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
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

	public void deleteUser(Integer id) {
		getMemberById(id);
		memberRepository.deleteById(id);

	}

}
