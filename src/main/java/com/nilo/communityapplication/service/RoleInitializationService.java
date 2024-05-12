package com.nilo.communityapplication.service;

import com.nilo.communityapplication.model.UserCommunityRole;
import com.nilo.communityapplication.repository.UserCommunityRoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleInitializationService {

    private final UserCommunityRoleRepository roleRepository;

    @PostConstruct
    public void initializeRoles() {
        // Check if roles already exist in the database
        if (roleRepository.count() < 3) {
            // Create and save roles if they don't exist
            saveRoleIfNotExists("OWNER");
            saveRoleIfNotExists("USER");
            saveRoleIfNotExists("MODERATOR");
        }
    }

    private void saveRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            UserCommunityRole role = new UserCommunityRole();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
