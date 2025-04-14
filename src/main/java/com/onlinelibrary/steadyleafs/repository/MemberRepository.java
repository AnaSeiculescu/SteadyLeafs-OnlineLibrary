package com.onlinelibrary.steadyleafs.repository;

import com.onlinelibrary.steadyleafs.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
	Optional<Member> findByUserId(Integer id);

	@Query("SELECT m FROM Member m LEFT JOIN FETCH m.borrowedBooks WHERE m.id = :id")
	Optional<Member> findByIdWithBorrowedBooks(@Param("id") int id);
}
