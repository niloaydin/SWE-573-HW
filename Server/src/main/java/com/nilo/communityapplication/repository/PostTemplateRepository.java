package com.nilo.communityapplication.repository;

import com.nilo.communityapplication.model.PostTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTemplateRepository extends JpaRepository<PostTemplate, Long> {
    PostTemplate findByName(String defaultTemplate);
}
