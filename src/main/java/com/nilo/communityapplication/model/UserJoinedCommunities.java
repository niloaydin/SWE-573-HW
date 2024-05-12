package com.nilo.communityapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_joined_communities")
public class UserJoinedCommunities {
    @EmbeddedId
    private CommunityJoinCompositeKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="user_id")
    @JsonBackReference("user-community-join")
    private User user;

    @ManyToOne
    @MapsId("communityId")
    @JoinColumn(name="community_id")
    @JsonBackReference("community-join")
    private Community community;

    @ManyToOne
    @JoinColumn(name="role_id")
    private UserCommunityRole role;

}
