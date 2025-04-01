package com.onlinelibrary.steadyleafs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, unique = true)
	private String username;

	private String firstName;
	private String lastName;

	public User mapToUpdate(User userUpdated) {
		User user = new User();
		user.setId(userUpdated.getId());
		user.setUsername(userUpdated.getUsername());
		user.setFirstName(userUpdated.getFirstName());
		user.setLastName(userUpdated.getLastName());
		return user;
	}
}
