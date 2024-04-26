package com.nilo.communityapplication.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "communities")
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(nullable = false)
    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonBackReference("user-community-owner")
    @EqualsAndHashCode.Exclude
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "communityMembers",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JsonIdentityReference(alwaysAsId = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> members;

}
