package com.nilo.communityapplication.service;

import com.nilo.communityapplication.auth.AuthenticationService;
import com.nilo.communityapplication.model.entity.Community;
import com.nilo.communityapplication.model.entity.User;
import com.nilo.communityapplication.repository.CommunityRepository;
import com.nilo.communityapplication.repository.UserRepository;
import com.nilo.communityapplication.model.requests.CommunityRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.nilo.communityapplication.model.mapper.CommunityMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(CommunityService.class);
    private final CommunityMapper communityMapper;

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

    public CommunityDTO getCommunityById(Long communityId) {

        Community oguz = communityRepository.findById(communityId);

        CommunityDTO community = communityMapper.apply(oguz);


        return communityRepository.findById(communityId);
    }

}


