package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Librarian;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.RegistrationDto;
import com.onlinelibrary.steadyleafs.model.dto.UserReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.UserUpdateDto;
import com.onlinelibrary.steadyleafs.repository.LibrarianRepository;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import com.onlinelibrary.steadyleafs.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
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
	private UserService userService;

	@MockitoBean
	private UserRepository userRepository;

	@MockitoBean
	private PasswordEncoder passwordEncoder;

	@MockitoBean
	private MemberService memberService;

	@MockitoBean
	private MemberRepository memberRepository;

	@MockitoBean
	private LibrarianService librarianService;

	@MockitoBean
	private LibrarianRepository librarianRepository;

	@MockitoBean
	private Authentication authentication;

	@MockitoBean
	private MyUserDetails myUserDetails;

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
	void createUserGivenOneUserExpectUserCreated() {

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

	@Test
	void updateUserEmailChanges() {
		UserUpdateDto userUpdateDto = new UserUpdateDto();
		userUpdateDto.setId(1);
		userUpdateDto.setEmail("new@email.com");
		userUpdateDto.setRole("ROLE_MEMBER");

		User userFromDatabase = new User();
		userFromDatabase.setId(1);
		userFromDatabase.setEmail("old@email.com");
		userFromDatabase.setRole("ROLE_MEMBER");

		when(userRepository.findById(1))
				.thenReturn(Optional.of(userFromDatabase));
		when(userRepository.save(any(User.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		UserUpdateDto result = userService.updateUser(userUpdateDto);

		assertEquals("new@email.com", result.getEmail());
		assertEquals("ROLE_MEMBER", result.getRole());
	}

	@Test
	void updateUserWhenUserNotFoundExpectException() {
		UserUpdateDto userUpdateDto = new UserUpdateDto();
		userUpdateDto.setId(95);
		userUpdateDto.setEmail("new@email.com");

		when(userRepository.findById(95))
				.thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(userUpdateDto));
		assertEquals("User with id 95 does not exists", exception.getMessage());
	}

	@Test
	void convertMemberToLibrarian_IfTheUsersRoleChangesToLibrarianALibrarianIsCreated() {
		Member member = new Member();
		member.setId(5);

		User user = new User();
		user.setId(1);
		user.setMember(member);
		member.setUser(user);

		Librarian librarian = new Librarian();
		librarian.setId(3);
		librarian.setUser(user);

		when(librarianService.createLibrarian(member))
				.thenReturn(librarian);

		when(userRepository.save(any(User.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		User result = userService.convertMemberToLibrarian(user);

		assertNull(result.getMember());
		assertEquals(librarian, result.getLibrarian());

		verify(userRepository, times(1)).save(user);
		verify(librarianRepository, times(1)).save(librarian);
		verify(librarianService).createLibrarian(member);
		verify(memberService).deleteMember(member.getId());
	}

	@Test
	void convertMemberToLibrarian_GivenUserWithoutMemberExpectException() {
		User user = new User();

		Exception exception = assertThrows(IllegalStateException.class, () -> userService.convertMemberToLibrarian(user));

		assertEquals("Member not found. User is not a member.", exception.getMessage());
	}

	@Test
	void convertLibrarianToMember_IfTheUsersRoleChangesToMemberAMemberIsCreated() {
		Librarian librarian = new Librarian();
		librarian.setId(5);

		User user = new User();
		user.setId(1);
		user.setLibrarian(librarian);
		librarian.setUser(user);

		Member member = new Member();
		member.setId(3);
		member.setUser(user);

		when(memberService.createMemberFromLibrarian(librarian))
				.thenReturn(member);

		when(userRepository.save(any(User.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		User result = userService.convertLibrarianToMember(user);

		assertNull(result.getLibrarian());
		assertEquals(member, result.getMember());

		verify(userRepository, times(1)).save(user);
		verify(memberRepository, times(1)).save(member);
		verify(memberService).createMemberFromLibrarian(librarian);
		verify(librarianService).deleteLibrarian(librarian.getId());
	}

	@Test
	void convertLibrarianToMember_GivenUserWithoutLibrarianExpectException() {
		User user = new User();

		Exception exception = assertThrows(IllegalStateException.class, () -> userService.convertLibrarianToMember(user));

		assertEquals("Librarian not found. User is not a librarian.", exception.getMessage());
	}

	@Test
	void deleteUserWhenUserExists() {
		User user = new User();
		user.setId(1);
		user.setEmail("user@email.com");

		when(userRepository.findById(1))
				.thenReturn(Optional.of(user));

		userService.deleteUser(1);

		verify(userRepository).deleteById(1);
	}

	@Test
	void deleteUserWhenUserDoesNotExistsThrowsException() {
		when(userRepository.findById(95))
				.thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(95));
		assertEquals("User with id 95 does not exists", exception.getMessage());

		verify(userRepository, never()).deleteById(95);
	}

	@Test
	void deleteUserWhenNullInputExpectException() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
		assertEquals("ID cannot be null", exception.getMessage());

		verify(userRepository, never()).deleteById(any());
	}

	@Test
	void deleteUserWhenNegativeInputExpectException() {
		when(userRepository.findById(-1))
				.thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(-1));
		assertEquals("ID cannot be negative", exception.getMessage());

		verify(userRepository, never()).deleteById(-1);
	}

	@Test
	void getLoggedInUserExpectCorrectUser() {
		User expectedUser = new User();
		expectedUser.setId(1);
		expectedUser.setEmail("user@email.com");

		when(myUserDetails.getUser())
				.thenReturn(expectedUser);
		when(authentication.getPrincipal())
				.thenReturn(myUserDetails);

		User actualUser = userService.getLoggedInUser(authentication);

		assertEquals(expectedUser, actualUser);
	}
}