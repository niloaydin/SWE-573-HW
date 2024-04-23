package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    List<User> findAll();

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.communities")
    List<User> findAllWithCommunities();

}
