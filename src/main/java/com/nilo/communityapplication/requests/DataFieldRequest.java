package com.nilo.communityapplication.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nilo.communityapplication.model.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataFieldRequest {
    private String name;
    private FieldType type;
    @JsonProperty("isRequired")
    private boolean isRequired;

}
