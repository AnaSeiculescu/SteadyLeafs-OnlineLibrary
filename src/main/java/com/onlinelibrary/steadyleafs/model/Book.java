package com.onlinelibrary.steadyleafs.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	private String author;
	private String coverUrl;
	private String status;
	private LocalDate dueDate;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member borrowedBy;

	public void setBorrowedBy(Member member) {
		this.borrowedBy = member;
		this.status = (member == null) ? "available" : "BORROWED";
	}

}
