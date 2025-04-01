package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public void createUser(User user) {
		userRepository.save(user);
	}

	public User updateUser(User user) {
		User userToUpdate = userRepository.findById(user.getId())
				.orElseThrow(() -> new RuntimeException("User with id " + user.getId() + " does not exists"));

		return userRepository.save(userToUpdate.mapToUpdate(user));
	}

	public User findUserById(int id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User with id " + id + " does not exists"));
		return user;
	}

}
