package com.nilo.communityapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDTO {
    private Long id;
    private String name;
    private String description;
    private boolean isPublic;
    private UserInCommunityDTO owner;
/*    private List<UserInCommunityDTO> members;
    private List<PostInCommunityDTO> communityPosts;*/
}