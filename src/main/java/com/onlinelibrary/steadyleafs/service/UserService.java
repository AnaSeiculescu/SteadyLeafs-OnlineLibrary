package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.UserReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.UserUpdateDto;
import com.onlinelibrary.steadyleafs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public List<UserReturnDto> getAllUsers() {
		List<User> usersFromDatabase = userRepository.findAll();

		return usersFromDatabase.stream()
				.map(user -> new UserReturnDto().mapFromUser(user))
				.toList();
	}

	public void createUser(User user) {
		userRepository.save(user);
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

		return UserUpdateDto.mapFromUser(updatedUser);
	}

	public void deleteUser(Integer id) {
		getUserById(id);
		userRepository.deleteById(id);
	}

}
