package com.onlinelibrary.steadyleafs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@OneToMany(mappedBy = "borrowedBy", cascade = CascadeType.ALL)
	private List<Book> borrowedBooks;

	public Member(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
