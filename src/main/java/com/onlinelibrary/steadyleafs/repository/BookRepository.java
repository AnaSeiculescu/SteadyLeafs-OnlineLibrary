package com.onlinelibrary.steadyleafs.repository;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	List<Book> findByBorrowedByIsNull(Sort sort);

	List<Book> findByBorrowedByIsNotNull(Sort sort);

//	List<Book> findByBorrowedBy(Member member);

	List<Book> findByBorrowedBy_Id(Integer memberId);
}
