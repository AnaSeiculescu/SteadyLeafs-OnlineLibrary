package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.RegistrationDto;
import com.onlinelibrary.steadyleafs.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

	@InjectMocks
	@Autowired
	UserService userService;

	@MockitoBean
	UserRepository userRepository;

	@MockitoBean
	private PasswordEncoder passwordEncoder;

	@MockitoBean
	MemberService memberService;

	@Test
	void createUserWithNoDataExpectException() {
		RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(null));
		assertEquals("Missing new registration data", exception.getMessage());
	}

	@Test
	void createUserGivenInvalidInputExpectException() {

		RegistrationDto invalidUserDto = new RegistrationDto();
		invalidUserDto.setEmail("user@email.com");
		invalidUserDto.setPassword("pass");
		invalidUserDto.setFirstName("Vasile%$");
		invalidUserDto.setLastName("Popescu");

		when(passwordEncoder.encode(any())).thenReturn("encoded-pass");

		User user = new User();
		user.setEmail("user@email.com");
		user.setPassword("pass");
		user.setRole("ROLE_MEMBER");

		when(userRepository.save(any())).thenReturn(user);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(invalidUserDto));
		assertEquals("First name must use valid characters", exception.getMessage());
	}

	@Test
	void createUserGivenOneBookExpectUserCreated() {

		RegistrationDto userDto = new RegistrationDto();
		userDto.setEmail("user@email.com");
		userDto.setPassword("pass");
		userDto.setFirstName("Vasile");
		userDto.setLastName("Popescu");

		when(passwordEncoder.encode(any())).thenReturn("encoded-pass");

		User expectedUser = new User();
		expectedUser.setEmail("user@email.com");
		expectedUser.setPassword("encoded-pass");
		expectedUser.setRole("ROLE_MEMBER");

		when(userRepository.save(any())).thenReturn(expectedUser);
		User createdUser = userService.createUser(userDto);

		assertEquals(expectedUser, createdUser);
		verify(memberService).createMember(any(Member.class));
	}
}