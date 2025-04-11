package com.onlinelibrary.steadyleafs.repository;

import com.onlinelibrary.steadyleafs.model.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Integer> {
}
