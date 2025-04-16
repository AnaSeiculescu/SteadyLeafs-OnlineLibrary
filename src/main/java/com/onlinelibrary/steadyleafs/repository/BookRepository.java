package com.onlinelibrary.steadyleafs.repository;

import com.onlinelibrary.steadyleafs.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	List<Book> findByBorrowedByIsNull();

	List<Book> findByBorrowedByIsNotNull();


}
