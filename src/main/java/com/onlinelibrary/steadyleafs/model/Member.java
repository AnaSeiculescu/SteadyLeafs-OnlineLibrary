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

	@OneToOne
	@JoinColumn(name =  "user_id")
	private User user;

	@OneToMany(mappedBy = "borrowedBy", cascade = CascadeType.ALL)
	private List<Book> borrowedBooks = new ArrayList<>();

//	public Member(String firstName, String lastName) {
//		this.firstName = firstName;
//		this.lastName = lastName;
//	}

//	public Member mapToUpdate(Member memberUpdated) {
//		Member member = new Member();
//		member.setId(memberUpdated.getId());
//		member.setFirstName(memberUpdated.getFirstName());
//		member.setPassword(memberUpdated.getPassword());
//		user.setRole(userUpdated.getRole());
//		return member;
//	}
}
