package com.example.demo.Models;
/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name="userPosts")
public class UserPost {

    private static final Logger logger = LoggerFactory.getLogger(UserPost.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ElementCollection
    @CollectionTable(name = "user_post_names", joinColumns = @JoinColumn(name = "user_post_id"))
    @Column(name = "name")
    private List<String> names;


    @ManyToOne
    @JoinColumn(name = "profileId")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "countryId")
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

    @ElementCollection
    @CollectionTable(name = "user_post_names", joinColumns = @JoinColumn(name = "user_post_id"))
    @Column(name = "contentTypes")
    private List<String> contentTypes;

    @Column(nullable = false)
    private String path;

    @Column
    private int shares;

    @Column
    private int favorites;


}