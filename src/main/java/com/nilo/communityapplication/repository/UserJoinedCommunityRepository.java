package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.DTO.UserInCommunityDTO;
import com.nilo.communityapplication.model.CommunityJoinCompositeKey;
import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.model.UserJoinedCommunities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJoinedCommunityRepository extends JpaRepository<UserJoinedCommunities, CommunityJoinCompositeKey> {

    List<UserJoinedCommunities> findByCommunityId(Long communityId);
}
