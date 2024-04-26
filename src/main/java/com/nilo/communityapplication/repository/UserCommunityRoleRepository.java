package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.model.UserCommunityRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCommunityRoleRepository extends JpaRepository<UserCommunityRole, Long> {
    boolean existsByName(String roleName);

    Optional<UserCommunityRole> findByName(String owner);

    @Query(value = "SELECT c " +
            "FROM UserJoinedCommunities jc, Community c, User u" +
            "\twhere jc.user.id=u.id\n" +
            "\t  and jc.community.id=c.id\n" +
            "\t  and jc.role.id=:param")
    List<Community> deneme(@Param("param") Long roleId );
}