package com.nilo.communityapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedUserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String avatar;
    private String jwt;

}
