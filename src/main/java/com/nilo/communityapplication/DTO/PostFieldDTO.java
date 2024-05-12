package com.nilo.communityapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostFieldDTO {
    private String fieldName;
    private String fieldValue;
}
