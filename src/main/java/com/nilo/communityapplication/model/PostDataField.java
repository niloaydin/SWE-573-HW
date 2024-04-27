package com.nilo.communityapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

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

    private String value;

    @Column(nullable = false)
    private boolean isRequired;

    @ManyToOne
    @JoinColumn(name="template_id")
    @JsonBackReference("post_template")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private PostTemplate postTemplate;

}
