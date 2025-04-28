package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.config.SecurityConfig;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.RegistrationDto;
import com.onlinelibrary.steadyleafs.model.dto.UserReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.UserUpdateDto;
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
		userRepository.save(registrationDto.mapToUser(securityConfig.delegatingPasswordEncoder()));
		memberService.createMember(registrationDto.mapToMember());
	}

	public User getUserById(Integer id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User with id " + id + " does not exists"));
		return user;
	}

	public UserUpdateDto updateUser(UserUpdateDto userUpdateDto) {
		User userToUpdate = userRepository.findById(userUpdateDto.getId())
				.orElseThrow(() -> new RuntimeException("User with id " + userUpdateDto.getId() + " does not exists"));

		if (userUpdateDto.getRole().equals("LIBRARIAN")) {
			convertMemberToLibrarian(userToUpdate);
		}

		User updatedUser = userUpdateDto.mapToUser(userToUpdate);
		userRepository.save(updatedUser);

		return UserUpdateDto.mapFromUser(updatedUser);
	}

	public void convertMemberToLibrarian(User user) {
		Member member = user.getMember();
		user.setMember(null);
		userRepository.save(user);

		librarianService.createLibrarian(member);
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
