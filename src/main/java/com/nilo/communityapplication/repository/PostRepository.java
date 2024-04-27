package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p " +
            "JOIN FETCH p.template t " +
            "JOIN FETCH t.datafields df " +
            "WHERE p.community.id = :communityId")
    List<Post> findByCommunityIdWithFields(@Param("communityId") Long communityId);
}
