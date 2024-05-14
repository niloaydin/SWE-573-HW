package com.nilo.communityapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostInCommunityDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Map<String, String> fieldDTOs;
    private UserInCommunityDTO userInCommunity;
}
