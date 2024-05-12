package com.nilo.communityapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="post_data_field")
public class PostDataField {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    private String type;

    @Column(nullable = false)
    private boolean isRequired;

    @ManyToOne
    @JoinColumn(name="template_id")
    @JsonBackReference("post_template")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private PostTemplate postTemplate;

/*    @OneToMany(mappedBy = "postDataField", fetch = FetchType.LAZY)
    @JsonManagedReference("post-data-value")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<PostFieldValue> values;*/

}
