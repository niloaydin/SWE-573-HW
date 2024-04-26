package com.nilo.communityapplication.service;

import com.nilo.communityapplication.auth.AuthenticationService;
import com.nilo.communityapplication.auth.config.JwtService;
import com.nilo.communityapplication.model.*;
import com.nilo.communityapplication.repository.CommunityRepository;
import com.nilo.communityapplication.repository.UserCommunityRoleRepository;
import com.nilo.communityapplication.repository.UserJoinedCommunityRepository;
import com.nilo.communityapplication.repository.UserRepository;
import com.nilo.communityapplication.requests.CommunityRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final UserJoinedCommunityRepository userJoinedCommunityRepository;
    private final UserCommunityRoleRepository userCommunityRoleRepository;
    private static final Logger logger = LoggerFactory.getLogger(CommunityService.class);

    @Transactional
    public Community createCommunity(CommunityRequest communityRequest) throws Exception {
        // Get authenticated user details
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Community newCommunity = Community.builder()
                .name(communityRequest.getName())
                .description(communityRequest.getDescription())
                .isPublic(communityRequest.isPublic())
                .build();
        newCommunity.setOwner(userDetails);
        Community savedCommunity = communityRepository.save(newCommunity);

        // Update user's communities
        userDetails.getCommunities().add(savedCommunity);
        userRepository.save(userDetails);

        logger.info("SAVED COMMUNITY {}", savedCommunity);
        logger.info("DOES USER HAVE COMMUNITY {}", userDetails.getCommunities());

        return savedCommunity;
    }
    public List<Community> getAllCommunities(){
        List<Community> communities = communityRepository.findAll();
        System.out.println("Retrieved users: " + communities.size()); // Logging
        return communities;
    }
    public Optional<Community> getCommunityById(Long communityId) {
        return communityRepository.findById(communityId);
    }

    public void joinCommunity(Long communityId) {
        try {
            // Get the authenticated user details
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            logger.info("COMMUNITY SERVICE USER {}", user);

            // Get the community entity
            Community community = communityRepository.findById(communityId)
                    .orElseThrow(() -> new RuntimeException("Community not found"));
            logger.info("COMMUNITY SERVICE COMMUNITY {}", user);

            // Create a new UserJoinedCommunities entity
            UserJoinedCommunities userJoinedCommunities = new UserJoinedCommunities();
            UserCommunityRole userRole = new UserCommunityRole();
            userRole.setName("user");
            userCommunityRoleRepository.save(userRole);

            // Create the composite key
            CommunityJoinCompositeKey key = new CommunityJoinCompositeKey();
            key.setUserId(user.getId());
            key.setCommunityId(communityId);
            userJoinedCommunities.setId(key);

            // Set the user and community properties
            userJoinedCommunities.setUser(user);
            userJoinedCommunities.setCommunity(community);
            userJoinedCommunities.setRole(userRole);

            // Save the UserJoinedCommunities entity
            userJoinedCommunityRepository.save(userJoinedCommunities);

        } catch (Exception e){
            // Log the exception for debugging purposes
            logger.error("An error occurred while joining the community: {}", e.getMessage());

            // Rethrow the exception or handle it as needed
            throw e;
        }



    }

}


