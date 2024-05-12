package com.nilo.communityapplication.service;

import com.nilo.communityapplication.DTO.UserInCommunityDTO;
import com.nilo.communityapplication.model.UserJoinedCommunities;
import com.nilo.communityapplication.repository.UserJoinedCommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserJoinedCommunityService {

    private final UserJoinedCommunityRepository userJoinedCommunityRepository;
    public List<UserInCommunityDTO> findUsersWithRoleByCommunityId(Long communityId) {
        // Fetch UserJoinedCommunities entities by communityId
        List<UserJoinedCommunities> userJoinedCommunitiesList = userJoinedCommunityRepository.findByCommunityId(communityId);

        // Map UserJoinedCommunities entities to UserInCommunityDTO objects
        List<UserInCommunityDTO> userInCommunityDTOList = userJoinedCommunitiesList.stream()
                .map(userJoinedCommunities -> {
                    UserInCommunityDTO userInCommunityDTO = new UserInCommunityDTO();
                    userInCommunityDTO.setUserId(userJoinedCommunities.getUser().getId());
                    userInCommunityDTO.setUsername(userJoinedCommunities.getUser().getUsername());
                    userInCommunityDTO.setFirstName(userJoinedCommunities.getUser().getFirstName());
                    userInCommunityDTO.setLastName(userJoinedCommunities.getUser().getLastName());
                    userInCommunityDTO.setRoleName(userJoinedCommunities.getRole().getName());
                    return userInCommunityDTO;
                })
                .collect(Collectors.toList());

        return userInCommunityDTOList;
    }
}
