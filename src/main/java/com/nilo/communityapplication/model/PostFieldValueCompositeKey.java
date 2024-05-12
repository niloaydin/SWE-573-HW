package com.nilo.communityapplication.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
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
public class PostFieldValueCompositeKey implements Serializable {
    private Long postId;
    private Long dataFieldId;

}
