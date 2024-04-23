package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community,Long> {

}
