package com.onlinelibrary.steadyleafs.repository;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberHomeRepository extends JpaRepository<Member, Integer> {

//	List<Book> findBorrowedBookByIdIn(List<Integer> bookIds);
}
