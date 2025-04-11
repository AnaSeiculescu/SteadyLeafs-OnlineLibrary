package com.onlinelibrary.steadyleafs.repository;

import com.onlinelibrary.steadyleafs.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
	Optional<Member> findByUserId(Integer id);

}
