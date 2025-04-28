package com.onlinelibrary.steadyleafs.repository;

import com.onlinelibrary.steadyleafs.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibrarianHomeRepository extends JpaRepository<Member, Integer> {
}
