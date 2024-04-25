package com.nilo.communityapplication.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityDTO {
    private Long id;
    private String name;
    private String description;
    private boolean isPublic;
    private Long ownerId;
    private Set<UserDTO> members;
}
