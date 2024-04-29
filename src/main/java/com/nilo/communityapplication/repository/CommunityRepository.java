package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community,Long> {
    List<Community> findAll();
/*    Community findById(Long communityId);*/


}
