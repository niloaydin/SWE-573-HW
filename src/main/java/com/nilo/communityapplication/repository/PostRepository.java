package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.community c " +
            "LEFT JOIN FETCH p.template t " +
            "LEFT JOIN FETCH t.datafields " +
            "WHERE c.id = :communityId")
    List<Post> findByCommunityIdWithFields(@Param("communityId") Long communityId);
}
