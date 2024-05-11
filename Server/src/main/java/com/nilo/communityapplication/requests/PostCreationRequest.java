package com.nilo.communityapplication.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCreationRequest {
    private Long userId;
    private Long communityId;
    private Long templateId;
    private Map<String, String> dataValues;
}
