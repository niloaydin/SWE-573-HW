package com.nilo.communityapplication.service;

import com.nilo.communityapplication.auth.AuthenticationService;
import com.nilo.communityapplication.auth.config.JwtService;
import com.nilo.communityapplication.model.Community;
import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.repository.CommunityRepository;
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

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
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

}


