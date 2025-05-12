package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.BookCreateDto;
import com.onlinelibrary.steadyleafs.model.dto.MemberReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.UserReturnDto;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
}