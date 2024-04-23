package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community,Long> {
    List<Community> findAll();
}
