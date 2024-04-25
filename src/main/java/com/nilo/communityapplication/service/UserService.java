package com.nilo.communityapplication.service;

import com.nilo.communityapplication.model.entity.Community;
import com.nilo.communityapplication.model.entity.User;
import com.nilo.communityapplication.repository.CommunityRepository;
import com.nilo.communityapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CommunityService communityService;
    private final CommunityRepository communityRepository;

    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();
        System.out.println("Retrieved users: " + users.size()); // Logging
        return users;
    }

    public List<User> getAllUsersWithCommunities() {
        List<User> users = userRepository.findAllWithCommunities();
        System.out.println("Retrieved users: " + users.size());
        return users;
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public void joinCommunity(Long communityId) {
        // Get the authenticated user details
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the community entity
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        // Add the user to the community's members

        // Add the community to the user's joinedCommunities
        user.getJoinedCommunities().add(community);

        // Update both entities
        communityRepository.save(community);
        userRepository.save(user);
    }
}
