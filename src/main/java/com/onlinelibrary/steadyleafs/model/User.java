package com.onlinelibrary.steadyleafs.model;

import jakarta.persistence.*;
import lombok.*;

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

	private String firstName;
	private String lastName;

	public User mapToUpdate(User userUpdated) {
		User user = new User();
		user.setId(userUpdated.getId());
		user.setEmail(userUpdated.getEmail());
		user.setFirstName(userUpdated.getFirstName());
		user.setLastName(userUpdated.getLastName());
		return user;
	}
}
