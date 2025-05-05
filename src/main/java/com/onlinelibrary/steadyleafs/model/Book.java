package com.onlinelibrary.steadyleafs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	private String author;
	private String status;
//	private LocalDate dueDate;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member borrowedBy;

	public void setBorrowedBy(Member member) {
		this.borrowedBy = member;
		this.status = (member == null) ? "available" : "BORROWED";
	}

}
