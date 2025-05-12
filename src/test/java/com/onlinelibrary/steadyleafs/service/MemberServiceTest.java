package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.MemberReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.MemberUpdateDto;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class MemberServiceTest {

	@InjectMocks
	@Autowired
	MemberService memberService;

	@MockitoBean
	MemberRepository memberRepository;

	@Test
	void createMemberWhenNoDataExpectException() {
		RuntimeException exception = assertThrows(RuntimeException.class, () -> memberService.createMember(null));
		assertEquals("Missing member data", exception.getMessage());
	}

	@Test
	void createMemberGivenOneMemberExpectMemberCreated() {
		User user = new User();
		user.setId(1);

		Member member = new Member();
		member.setFirstName("Vasile");
		member.setLastName("Popescu%$");
		member.setUser(user);

		when(memberRepository.save(any()))
				.thenReturn(member);

		Member expectedMember = memberService.createMember(member);

		assertEquals(expectedMember, member);
	}

	@Test
	void getAllMembersCreateTwoMembersExpectTwoMembersFromRepo() {

		User user1 = new User();
		user1.setId(1);

		User user2 = new User();
		user2.setId(2);

		Member member1 = new Member();
		member1.setId(3);
		member1.setFirstName("Vasile");
		member1.setLastName("Popescu");
		member1.setUser(user1);

		Member member2 = new Member();
		member2.setId(4);
		member2.setFirstName("Ion");
		member2.setLastName("Carstea");
		member2.setUser(user2);

		List<Member> mockedMembers = List.of(member1, member2);

		when(memberRepository.findAll())
				.thenReturn(mockedMembers);

		List<MemberReturnDto> result = memberService.getAllMembers();

		assertEquals(2, result.size());
		assertEquals("Ion", result.get(1).getFirstName());
		assertEquals("Popescu", result.get(0).getLastName());
	}

	@Test
	void getAllMembersWhenNoInputExpectEmptyList() {
		when(memberRepository.findAll())
				.thenReturn(List.of());

		List<MemberReturnDto> result = memberService.getAllMembers();

		assertEquals(0, result.size());
	}

	@Test
	void getMemberByIdWhenMemberExistsReturnsMappedDto() {
		Member member = new Member();
		member.setId(1);
		member.setFirstName("Vasile");
		member.setLastName("Popescu");

		when(memberRepository.findById(1))
				.thenReturn(Optional.of(member));

		MemberReturnDto userReturnDto = memberService.getMemberById(1);

		assertEquals("Vasile", userReturnDto.getFirstName());
		assertEquals("Popescu", userReturnDto.getLastName());
	}

	@Test
	void getMemberByIdWhenMemberDoesNotExistsExpectException() {
		when(memberRepository.findById(95))
				.thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> memberService.getMemberById(95));
		assertEquals("Member with id 95 does not exists", exception.getMessage());
	}

	@Test
	void getMemberByIdWhenNullInputExpectException() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> memberService.getMemberById(null));
		assertEquals("ID cannot be null", exception.getMessage());

		verify(memberRepository, never()).findById(any());
	}

	@Test
	void getMemberByIdWhenNegativeInputExpectException() {
		when(memberRepository.findById(-1)).thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> memberService.getMemberById(-1));
		assertEquals("ID cannot be negative", exception.getMessage());

		verify(memberRepository, never()).findById(-1);
	}

	@Test
	void updateMemberFirstNameChanges() {
		MemberUpdateDto memberUpdateDto = new MemberUpdateDto();
		memberUpdateDto.setId(1);
		memberUpdateDto.setFirstName("Gigel");
		memberUpdateDto.setLastName("Popescu");

		Member memberFromDatabase = new Member();
		memberFromDatabase.setId(1);
		memberFromDatabase.setFirstName("Vasile");
		memberFromDatabase.setLastName("Popescu");

		when(memberRepository.findById(1))
				.thenReturn(Optional.of(memberFromDatabase));
		when(memberRepository.save(any(Member.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		MemberUpdateDto result = memberService.updateMember(memberUpdateDto);

		assertEquals("Gigel", result.getFirstName());
		assertEquals("Popescu", result.getLastName());
	}

	@Test
	void updateMemberWhenMemberNotFoundExpectException() {
		MemberUpdateDto memberUpdateDto = new MemberUpdateDto();
		memberUpdateDto.setId(95);
		memberUpdateDto.setFirstName("Vasile");
		memberUpdateDto.setLastName("Popescu");

		when(memberRepository.findById(95))
				.thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> memberService.updateMember(memberUpdateDto));
		assertEquals("Member with id 95 does not exists", exception.getMessage());
	}

	@Test
	void deleteMemberWhenMemberExists() {
		Member member = new Member();
		member.setId(1);
		member.setFirstName("Vasile");

		when(memberRepository.findById(1))
				.thenReturn(Optional.of(member));

		memberService.deleteMember(1);

		verify(memberRepository).deleteById(1);
	}

	@Test
	void deleteMemberWhenMemberDoesNotExistsThrowsException() {
		when(memberRepository.findById(95))
				.thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> memberService.deleteMember(95));
		assertEquals("Member with id 95 does not exists", exception.getMessage());

		verify(memberRepository, never()).deleteById(95);
	}

	@Test
	void deleteMemberWhenNullInputExpectException() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> memberService.deleteMember(null));
		assertEquals("ID cannot be null", exception.getMessage());

		verify(memberRepository, never()).deleteById(any());
	}

	@Test
	void deleteMemberWhenNegativeInputExpectException() {
		when(memberRepository.findById(-1))
				.thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> memberService.deleteMember(-1));
		assertEquals("ID cannot be negative", exception.getMessage());

		verify(memberRepository, never()).deleteById(-1);
	}

	@Test
	void getMemberByUserIdGivenOneUserExpectMember() {
		User user = new User();
		user.setId(7);

		Member memberFromDatabase = new Member();
		memberFromDatabase.setFirstName("Vasile");
		memberFromDatabase.setLastName("Popescu");
		memberFromDatabase.setUser(user);

		when(memberRepository.findByUserId(7))
				.thenReturn(Optional.of(memberFromDatabase));

		MemberReturnDto result = memberService.getMemberByUserId(7);

		assertEquals("Vasile", result.getFirstName());
		assertEquals("Popescu", result.getLastName());
	}

	@Test
	void getMemberByUserIdWhenMemberDoesNotExistExpectException() {
		User user = new User();
		user.setId(7);

		when(memberRepository.findByUserId(7))
				.thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> memberService.getMemberByUserId(7));
		assertEquals("Member with user id: 7 not found", exception.getMessage());
	}
}