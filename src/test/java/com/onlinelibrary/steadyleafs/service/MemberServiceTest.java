package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

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
}