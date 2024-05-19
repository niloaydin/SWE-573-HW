package com.nilo.communityapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Long id;
    private LocalDateTime createdAt;
    private LinkedHashMap<String, String> content = new LinkedHashMap<>();
    private String templateName;
    private Long templateId;
    private CommunityDTO community;
    private UserInCommunityDTO created_by;
}
