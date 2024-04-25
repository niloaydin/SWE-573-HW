package com.nilo.communityapplication.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nilo.communityapplication.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityRequest {

    private String name;
    private String description;
    @JsonProperty("isPublic")
    private boolean isPublic;
    private User owner;

}
