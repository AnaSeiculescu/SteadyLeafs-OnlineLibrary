package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.config.SecurityConfig;
import com.onlinelibrary.steadyleafs.model.Librarian;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.RegistrationDto;
import com.onlinelibrary.steadyleafs.model.dto.UserReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.UserUpdateDto;
import com.onlinelibrary.steadyleafs.repository.LibrarianRepository;
import com.onlinelibrary.steadyleafs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final MemberService memberService;
	private final LibrarianService librarianService;

	@Autowired
	SecurityConfig securityConfig;

	public List<UserReturnDto> getAllUsers() {
		List<User> usersFromDatabase = userRepository.findAll();

		return usersFromDatabase.stream()
				.map(user -> new UserReturnDto().mapFromUser(user))
				.toList();
	}

	public void createUser(RegistrationDto registrationDto) {
		User user = userRepository.save(registrationDto.mapToUser(securityConfig.delegatingPasswordEncoder()));
		userRepository.save(user);

		Member member = registrationDto.mapToMember();
		member.setUser(user);
		memberService.createMember(member);
	}

	public User getUserById(Integer id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User with id " + id + " does not exists"));
		return user;
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

	public void convertMemberToLibrarian(User user) {
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
	}

	public void deleteUser(Integer id) {
		getUserById(id);
		userRepository.deleteById(id);
	}

	public User getLoggedInUser(Authentication authentication) {
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		User currentUser = userDetails.getUser();
		return currentUser;
	}

}
