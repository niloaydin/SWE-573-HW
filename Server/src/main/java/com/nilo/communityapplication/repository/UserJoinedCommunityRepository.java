package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.model.CommunityJoinCompositeKey;
import com.nilo.communityapplication.model.UserJoinedCommunities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJoinedCommunityRepository extends JpaRepository<UserJoinedCommunities, CommunityJoinCompositeKey> {

}
