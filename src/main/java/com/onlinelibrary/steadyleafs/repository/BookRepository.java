package com.onlinelibrary.steadyleafs.repository;

import com.onlinelibrary.steadyleafs.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	List<Book> findByBorrowedByIsNull(Sort sort);
	List<Book> findByBorrowedByIsNotNull(Sort sort);
	List<Book> findByBorrowedBy_Id(Integer memberId);
	List<Book> findByTitleContainingIgnoreCase(String title);
	List<Book> findByAuthorContainingIgnoreCase(String author);

	Page<Book> findAll(Pageable pageable);
	Page<Book> findByBorrowedByIsNull(Pageable pageable);
	Page<Book> findByBorrowedByIsNotNull(Pageable pageable);
}
