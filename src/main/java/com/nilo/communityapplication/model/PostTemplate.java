package com.nilo.communityapplication.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="post_template")
public class PostTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "postTemplate", fetch = FetchType.EAGER)
    @JsonManagedReference("post_template")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<PostDataField> datafields;
}
