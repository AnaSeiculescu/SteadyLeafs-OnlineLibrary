package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.Librarian;
import com.onlinelibrary.steadyleafs.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LibrarianCreateDto {

	// There is no need of this class, for now
	// Librarians are created from members, when the corresponding user changes role

	private String firstName;
	private String lastName;
	private User user;

	public Librarian mapToLibrarian() {
		Librarian librarian = new Librarian();
		librarian.setFirstName(this.firstName);
		librarian.setLastName(this.lastName);
		librarian.setUser(this.user);

		return librarian;
	}

}
