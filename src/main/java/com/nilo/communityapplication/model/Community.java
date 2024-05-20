package com.nilo.communityapplication.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.List;
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

    @Column(nullable = false, unique = true, length=1000)
    private String name;
    @Column(length = 5000)
    private String description;

    @Column(nullable = false)
    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonBackReference("user-community-owner")
    @EqualsAndHashCode.Exclude
    private User owner;

    @OneToMany(mappedBy = "community", fetch=FetchType.LAZY)
    @JsonManagedReference("community-join")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<UserJoinedCommunities> members;

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
    @JsonManagedReference("community-posts")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Post> communityPosts;


/*    @JsonIgnoreProperties("joinedCommunities")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "communityMembers",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> members;*/

}
