package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.config.SecurityConfig;
import com.onlinelibrary.steadyleafs.model.Librarian;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.RegistrationDto;
import com.onlinelibrary.steadyleafs.model.dto.UserReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.UserUpdateDto;
import com.onlinelibrary.steadyleafs.repository.LibrarianRepository;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import com.onlinelibrary.steadyleafs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final MemberRepository memberRepository;
	private final LibrarianRepository librarianRepository;
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
		memberRepository.save(registrationDto.mapToMember());
		
	}

	public User getUserById(Integer id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User with id " + id + " does not exists"));
		return user;
	}

	public UserUpdateDto updateUser(UserUpdateDto userUpdateDto) {
		User userToUpdate = userRepository.findById(userUpdateDto.getId())
				.orElseThrow(() -> new RuntimeException("User with id " + userUpdateDto.getId() + " does not exists"));

		User updatedUser = userUpdateDto.mapToUser(userToUpdate);
		userRepository.save(updatedUser);

		// aici ar trebui sa pot folosi un librarianService, care creeaza librarians, si adauga in librarianRepo
		// daca noul rol este diferit de "MEMBER" atunci membrul trebuie sters din repository-ul de member si adaugat in librarians

//		if(updatedUser.getRole().equals("LIBRARIAN")){
//			librarianRepository.save(new Librarian());
//		}

		return UserUpdateDto.mapFromUser(updatedUser);
	}

	public void deleteUser(Integer id) {
		getUserById(id);
		userRepository.deleteById(id);
	}

}
