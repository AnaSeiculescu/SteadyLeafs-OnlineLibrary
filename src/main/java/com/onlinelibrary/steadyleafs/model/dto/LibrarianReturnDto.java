package com.onlinelibrary.steadyleafs.model.dto;

import com.onlinelibrary.steadyleafs.model.Librarian;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LibrarianReturnDto {
	private int id;
	private String firstName;
	private String lastName;
	private Integer userId;

	public LibrarianReturnDto mapFromLibrarian(Librarian librarian) {
		LibrarianReturnDto librarianReturnDto = new LibrarianReturnDto();
		librarianReturnDto.setId(librarian.getId());
		librarianReturnDto.setFirstName(librarian.getFirstName());
		librarianReturnDto.setLastName(librarian.getLastName());

		if (librarian.getUser() != null) {
			librarianReturnDto.setUserId(librarian.getUser().getId());
		} else {
			System.out.println("Librarian with ID " + librarian.getId() + " has no associated User.");
		}

		return librarianReturnDto;
	}
}
