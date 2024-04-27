package com.nilo.communityapplication.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityJoinCompositeKey implements Serializable {
    private Long userId;
    private Long communityId;
}
