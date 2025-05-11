package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.config.SecurityConfig;
import com.onlinelibrary.steadyleafs.model.Librarian;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.RegistrationDto;
import com.onlinelibrary.steadyleafs.model.dto.UserReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.UserUpdateDto;
import com.onlinelibrary.steadyleafs.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final MemberService memberService;
	private final LibrarianService librarianService;

	@Autowired
	SecurityConfig securityConfig;

	private final Validator validator;

	public List<UserReturnDto> getAllUsers() {
		List<User> usersFromDatabase = userRepository.findAll();

		return usersFromDatabase.stream()
				.map(user -> new UserReturnDto().mapFromUser(user))
				.toList();
	}

	public User createUser(RegistrationDto registrationDto) {
		if (registrationDto == null) {
			throw new RuntimeException("Missing new registration data");
		}

		Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(registrationDto);
		if (!violations.isEmpty()) {
			throw new RuntimeException(violations.iterator().next().getMessage());
		}

		User user = userRepository.save(registrationDto.mapToUser(securityConfig.delegatingPasswordEncoder()));
		userRepository.save(user);

		Member member = registrationDto.mapToMember();
		member.setUser(user);
		memberService.createMember(member);
		return user;
	}

	public UserReturnDto getUserById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}
		if (id < 0) {
			throw new IllegalArgumentException("ID cannot be negative");
		}

		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User with id " + id + " does not exists"));

		UserReturnDto userReturnDto = new UserReturnDto();
		return userReturnDto.mapFromUser(user);
	}

	public UserUpdateDto updateUser(UserUpdateDto userUpdateDto) {
		User userFromDatabase = userRepository.findById(userUpdateDto.getId())
				.orElseThrow(() -> new RuntimeException("User with id " + userUpdateDto.getId() + " does not exists"));

		User updatedUser = userUpdateDto.mapToUser(userFromDatabase);

		if (userUpdateDto.getRole().equals("ROLE_LIBRARIAN")) {
			convertMemberToLibrarian(userFromDatabase);
		}

		userRepository.save(updatedUser);

		return UserUpdateDto.mapFromUser(updatedUser);
	}

	public User convertMemberToLibrarian(User user) {
		Member member = user.getMember();

		if (member == null) {
			throw new IllegalStateException("Member not found. User is not a member.");
		}

		user.setMember(null);
		member.setUser(null);
		userRepository.save(user);

		Librarian librarian = librarianService.createLibrarian(member);
		librarian.setUser(user);
		user.setLibrarian(librarian);
		userRepository.save(user);

		memberService.deleteMember(member.getId());

		return user;
	}

	public void deleteUser(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}
		if (id < 0) {
			throw new IllegalArgumentException("ID cannot be negative");
		}

		getUserById(id);
		userRepository.deleteById(id);
	}

	public User getLoggedInUser(Authentication authentication) {
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		User currentUser = userDetails.getUser();
		return currentUser;
	}

}
