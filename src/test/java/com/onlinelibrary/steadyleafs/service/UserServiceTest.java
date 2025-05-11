package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.BookReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.RegistrationDto;
import com.onlinelibrary.steadyleafs.model.dto.UserReturnDto;
import com.onlinelibrary.steadyleafs.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

		when(passwordEncoder.encode(any()))
				.thenReturn("encoded-pass");

		User user = new User();
		user.setEmail("user@email.com");
		user.setPassword("encoded-pass");
		user.setRole("ROLE_MEMBER");

		when(userRepository.save(any()))
				.thenReturn(user);

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

		when(passwordEncoder.encode(any()))
				.thenReturn("encoded-pass");

		User expectedUser = new User();
		expectedUser.setEmail("user@email.com");
		expectedUser.setPassword("encoded-pass");
		expectedUser.setRole("ROLE_MEMBER");

		when(userRepository.save(any())).thenReturn(expectedUser);
		User createdUser = userService.createUser(userDto);

		assertEquals(expectedUser, createdUser);
		verify(memberService).createMember(any(Member.class));
	}

	@Test
	void getAllUsersCreateTwoUsersExpectTwoUsersFromRepo() {

		User user1 = new User();
		user1.setEmail("user1@email.com");
		user1.setPassword("pass1");
		user1.setRole("ROLE_MEMBER");

		User user2 = new User();
		user2.setEmail("user2@email.com");
		user2.setPassword("pass2");
		user2.setRole("ROLE_MEMBER");

		List<User> mockedUsers = List.of(user1, user2);

		when(userRepository.findAll())
				.thenReturn(mockedUsers);

		List<UserReturnDto> result = userService.getAllUsers();

		assertEquals(2, result.size());
		assertEquals("user2@email.com", result.get(1).getEmail());
		assertEquals("user1@email.com", result.get(0).getEmail());
	}

	@Test
	void getAllUsersWhenNoInputExpectEmptyList() {
		when(userRepository.findAll())
				.thenReturn(List.of());

		List<UserReturnDto> result = userService.getAllUsers();

		assertEquals(0, result.size());
	}

	@Test
	void getUserByIdWhenUserExistsReturnsMappedDto() {
		User user = new User();
		user.setId(1);
		user.setEmail("user@email.com");
		user.setPassword("pass");
		user.setRole("ROLE_MEMBER");

		when(userRepository.findById(1))
				.thenReturn(Optional.of(user));

		UserReturnDto userReturnDto = userService.getUserById(1);

		assertEquals("user@email.com", userReturnDto.getEmail());
		assertEquals("ROLE_MEMBER", userReturnDto.getRole());
	}

	@Test
	void getUserByIdWhenUserDoesNotExistsExpectException() {
		when(userRepository.findById(95))
				.thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(95));
		assertEquals("User with id 95 does not exists", exception.getMessage());
	}

	@Test
	void getUserByIdWhenNullInputExpectException() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(null));
		assertEquals("ID cannot be null", exception.getMessage());

		verify(userRepository, never()).findById(any());
	}

	@Test
	void getUserByIdWhenNegativeInputExpectException() {
		when(userRepository.findById(-1)).thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(-1));
		assertEquals("ID cannot be negative", exception.getMessage());

		verify(userRepository, never()).findById(-1);
	}
}