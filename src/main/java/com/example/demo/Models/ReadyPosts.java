package com.example.demo.Models;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "readyPosts")
@SQLDelete(sql = "UPDATE ready_posts SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class ReadyPosts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profileId", nullable = false)
    private Profile profile;

    @ManyToMany
    @JoinTable(name = "readyUserPosts",joinColumns = @JoinColumn(name = "readyPostId"),inverseJoinColumns = @JoinColumn(name = "userPostId", nullable = false))
    private List<UserPost> userPosts;

    private boolean deleted = Boolean.FALSE;
}
