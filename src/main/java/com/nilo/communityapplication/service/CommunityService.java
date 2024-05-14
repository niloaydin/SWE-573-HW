package com.nilo.communityapplication.service;

import com.nilo.communityapplication.DTO.CommunityDTO;
import com.nilo.communityapplication.DTO.PostInCommunityDTO;
import com.nilo.communityapplication.DTO.UserInCommunityDTO;
import com.nilo.communityapplication.auth.AuthenticationService;
import com.nilo.communityapplication.auth.config.JwtService;
import com.nilo.communityapplication.globalExceptionHandling.NotAuthorizedException;
import com.nilo.communityapplication.globalExceptionHandling.NotFoundException;
import com.nilo.communityapplication.model.*;
import com.nilo.communityapplication.repository.*;
import com.nilo.communityapplication.requests.CommunityRequest;
import com.nilo.communityapplication.utils.BasicAuthorizationUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class  CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final UserJoinedCommunityRepository userJoinedCommunityRepository;
    private final UserCommunityRoleRepository userCommunityRoleRepository;

    private final PostRepository postRepository;
    private final BasicAuthorizationUtil authUtil;
    private static final Logger logger = LoggerFactory.getLogger(CommunityService.class);

    @Transactional
    public Community createCommunity(CommunityRequest communityRequest) throws Exception {
        // Get authenticated user details
        User userDetails = authUtil.getCurrentUser();


        UserCommunityRole ownerRole = userCommunityRoleRepository.findByName("OWNER")
                .orElseThrow(() -> new RuntimeException("Owner role not found"));

        Community newCommunity = Community.builder()
                .name(communityRequest.getName())
                .description(communityRequest.getDescription())
                .isPublic(communityRequest.isPublic())
                .build();
        newCommunity.setOwner(userDetails);
        Community savedCommunity = communityRepository.save(newCommunity);
        UserJoinedCommunities userJoinedCommunities = new UserJoinedCommunities();

        CommunityJoinCompositeKey key = new CommunityJoinCompositeKey();
        key.setUserId(userDetails.getId());
        key.setCommunityId(savedCommunity.getId());
        userJoinedCommunities.setId(key);

        userJoinedCommunities.setUser(userDetails);
        userJoinedCommunities.setCommunity(savedCommunity);
        userJoinedCommunities.setRole(ownerRole);

        // Save the UserJoinedCommunities entity
        userJoinedCommunityRepository.save(userJoinedCommunities);

        // Update user's communities
/*        userDetails.getCommunities().add(savedCommunity);
        userRepository.save(userDetails);*/

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
            UserCommunityRole userRole = userCommunityRoleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("User role not found"));

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
            logger.error("An error occurred while joining the community: {}", e.getMessage());
            throw e;
        }


    }

    public List<Community> ilkerAbi(Long roleId){
        return userCommunityRoleRepository.deneme(roleId);
    }

    public List<Post> getPostsInCommunity(Long communityId) {
        return postRepository.findByCommunityIdWithFields(communityId);
    }

    public Post getSinglePostInCommunity(Long postId, Long communityId){
        return postRepository.findByIdAndCommunityId(postId,communityId);
    }

    public List<CommunityDTO> getAllCommunitiesWithDetails() {
        List<Community> communities = communityRepository.findAll();
        List<CommunityDTO> communityDTOs = new ArrayList<>();

        for (Community community : communities) {
            CommunityDTO communityDTO = new CommunityDTO();
            communityDTO.setId(community.getId());
            communityDTO.setName(community.getName());
            communityDTO.setDescription(community.getDescription());
            communityDTO.setPublic(community.isPublic());
            communityDTOs.add(communityDTO);

        }
        return communityDTOs;
    }

    public CommunityDTO getSingleCommunity(Long id){
        Community community = communityRepository.findById(id).orElseThrow(() -> new NotFoundException("Community not found!"));

        CommunityDTO communityDTO = new CommunityDTO();

        communityDTO.setName(community.getName());
        communityDTO.setDescription(community.getDescription());
        communityDTO.setId(community.getId());
        communityDTO.setPublic(community.isPublic());

        return communityDTO;
    }

    public List<UserInCommunityDTO> getMembersOfCommunity(Long id){

        Community community = communityRepository.findById(id).orElseThrow(() -> new NotFoundException("Community not found!"));

        List<UserJoinedCommunities> communityMembersList = community.getMembers();

        // Map members to MemberDTO
        List<UserInCommunityDTO> communityMembers = new ArrayList<>();
        for (UserJoinedCommunities member : communityMembersList) {
            UserInCommunityDTO memberDTO = new UserInCommunityDTO();
            memberDTO.setUserId(member.getUser().getId());
            memberDTO.setUsername(member.getUser().getUsername());
            memberDTO.setFirstName(member.getUser().getFirstName());
            memberDTO.setLastName(member.getUser().getLastName());
            memberDTO.setRoleName(member.getRole().getName());
            communityMembers.add(memberDTO);
        }
        return communityMembers;

    }

    @Transactional
    public Community updateCommunity(Long communityId, CommunityRequest communityRequest) throws Exception {

        User currentUser = authUtil.getCurrentUser();

        Community existingCommunity = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException("Community not found with ID: " + communityId));

        if(!currentUser.getId().equals(existingCommunity.getOwner().getId())){
            throw new NotAuthorizedException("You are not the owner of this community!");
        }

        existingCommunity.setName(communityRequest.getName());
        existingCommunity.setDescription(communityRequest.getDescription());
        existingCommunity.setPublic(communityRequest.isPublic());

        return communityRepository.save(existingCommunity);
    }
}


