package com.nilo.communityapplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-posts")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "community_id")
    @JsonBackReference("community-posts")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Community community;

    @ManyToOne
    @JoinColumn(name = "template_id")
    @JsonBackReference("post-templates")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private PostTemplate template;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
