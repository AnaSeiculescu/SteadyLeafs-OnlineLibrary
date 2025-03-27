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
}
