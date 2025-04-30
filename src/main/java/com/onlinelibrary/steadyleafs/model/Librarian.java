package com.onlinelibrary.steadyleafs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="librarians")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Librarian {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String firstName;
	private String lastName;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	public static Librarian mapFromMember(Member member) {
		Librarian librarian = new Librarian();
//		librarian.setId(this.id);
		librarian.setFirstName(member.getFirstName());
		librarian.setLastName(member.getLastName());
		librarian.setUser(member.getUser());

		return librarian;
	}

}
