package com.nilo.communityapplication.model.mapper;

import com.nilo.communityapplication.model.dto.CommunityDTO;
import com.nilo.communityapplication.model.dto.UserDTO;
import com.nilo.communityapplication.model.entity.Community;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CommunityMapper implements Function<Community, CommunityDTO> {

    private final UserMapper userMapper;

    public CommunityMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public CommunityDTO apply(Community community) {

        Set<UserDTO> userDTOS = community.getMembers().stream().map(userMapper::apply).collect(Collectors.toSet());

        CommunityDTO communityDto = new CommunityDTO();
        communityDto.setId(community.getId());
        communityDto.setMembers(userDTOS);

        return communityDto;
    }
}
