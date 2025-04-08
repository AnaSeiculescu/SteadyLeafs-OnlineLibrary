package com.onlinelibrary.steadyleafs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
//	@Column(nullable = false, unique = true)
	private String email;
	private String password;
	private String role;

	public User mapToUpdate(User userUpdated) {
		User user = new User();
		user.setId(userUpdated.getId());
		user.setEmail(userUpdated.getEmail());
		user.setPassword(userUpdated.getPassword());
		user.setRole(userUpdated.getRole());
		return user;
	}
}
