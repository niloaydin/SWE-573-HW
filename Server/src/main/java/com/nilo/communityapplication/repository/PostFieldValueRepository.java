package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.model.PostFieldValue;
import com.nilo.communityapplication.model.PostFieldValueCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFieldValueRepository extends JpaRepository<PostFieldValue, PostFieldValueCompositeKey> {
}
