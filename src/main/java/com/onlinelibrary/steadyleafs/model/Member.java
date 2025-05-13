package com.onlinelibrary.steadyleafs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="members")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String firstName;
	private String lastName;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

//	@OneToMany(mappedBy = "borrowedBy", cascade = CascadeType.ALL, orphanRemoval = true)
	@OneToMany(mappedBy = "borrowedBy")
	private List<Book> borrowedBooks = new ArrayList<>();

	public static Member mapFromLibrarian(Librarian librarian) {
		Member member = new Member();
		member.setFirstName(librarian.getFirstName());
		member.setLastName(librarian.getLastName());

		if (librarian.getUser() != null) {
			member.setUser(librarian.getUser());
		} else {
			System.out.println("Librarian with ID " + librarian.getId() + " has no associated User.");
		}

		return member;
	}
}
