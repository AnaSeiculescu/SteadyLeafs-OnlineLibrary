package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Librarian;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.dto.LibrarianReturnDto;
import com.onlinelibrary.steadyleafs.repository.LibrarianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibrarianService {
	private final LibrarianRepository librarianRepository;

	public List<LibrarianReturnDto> getAllLibrarians() {
		List<Librarian> librariansFromDatabase = librarianRepository.findAll();
		return librariansFromDatabase.stream()
				.map(librarian -> new LibrarianReturnDto().mapFromLibrarian(librarian))
				.toList();
	}

	public Librarian createLibrarian(Member member) {
		Librarian librarian = Librarian.mapFromMember(member);
		librarian.setUser(member.getUser());
		librarianRepository.save(librarian);

		return librarian;
	}

	public void deleteLibrarian(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}
		if (id < 0) {
			throw new IllegalArgumentException("ID cannot be negative");
		}

		Librarian librarian = librarianRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Librarian with id " + id + " does not exists"));
		librarianRepository.deleteById(librarian.getId());
	}
}
