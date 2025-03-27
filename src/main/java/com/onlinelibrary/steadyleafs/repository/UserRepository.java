package com.onlinelibrary.steadyleafs.repository;

import com.onlinelibrary.steadyleafs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
