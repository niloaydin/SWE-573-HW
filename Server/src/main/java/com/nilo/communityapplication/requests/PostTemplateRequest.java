package com.nilo.communityapplication.requests;

import com.nilo.communityapplication.model.PostDataField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostTemplateRequest {
    private String templateName;
    private Set<DataFieldRequest> dataFields;

}
