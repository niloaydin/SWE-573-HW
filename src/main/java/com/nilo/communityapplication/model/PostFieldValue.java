package com.nilo.communityapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="post_field_values")
public class PostFieldValue {
    @EmbeddedId
    private PostFieldValueCompositeKey id;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name="post_id")
    @JsonBackReference("post-field-value")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Post post;

    @ManyToOne
    @MapsId("dataFieldId")
    @JoinColumn(name="data_field_id")
/*    @JsonBackReference("post-data-value")*/
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private PostDataField postDataField;

    private String value;


/*    datafield id
    value
            post id*/
}
