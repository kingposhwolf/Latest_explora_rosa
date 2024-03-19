package com.example.demo.Models;
/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Data
@Table(name="userPosts")
@SQLDelete(sql = "UPDATE user_posts SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class UserPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "countryId", nullable = false)
    private Country country;

    @ManyToMany
    @JoinTable(name = "userPostHashTag",joinColumns = @JoinColumn(name = "userPostId"),inverseJoinColumns = @JoinColumn(name = "hashTagId"))
    private List<HashTag> hashTags;

    @ManyToMany
    @JoinTable(name = "userPostTag",joinColumns = @JoinColumn(name = "userPostId"),inverseJoinColumns = @JoinColumn(name = "tagId"))
    private List<Tag> tags;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brandId")
    private Brand brand;

    @Column(nullable = false)
    private int likes;

    @Column(nullable = false)
    private int comments;

    private String caption;

    @Column
    private String thumbnail;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String path;

    @Column
    private int shares;

    @Column
    private int favorites;

    private boolean deleted = Boolean.FALSE;
}