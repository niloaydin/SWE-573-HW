package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.model.TokenBlockList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenBlockListRepository extends JpaRepository<TokenBlockList, Long> {
    boolean existsByToken(String token);
}
