package com.nilo.communityapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInCommunityDTO {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String roleName;
}
