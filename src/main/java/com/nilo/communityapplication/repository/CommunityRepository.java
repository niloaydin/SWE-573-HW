package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community,Long> {
    List<Community> findAll();
/*    Optional<Community> findById(String email);*/
}
