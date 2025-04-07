package com.onlinelibrary.steadyleafs.repository;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberHomeRepository extends JpaRepository<Member, Integer> {

	public Member createMember() {
		Member newMember = new Member();
		newMember.setFirstName(firstName);
		newMember.setLastName(lastName);

		// Save the new member to the repository
		return memberHomeRepository.save(newMember);
	}
}
