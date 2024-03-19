package com.example.demo.Models;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Services.CommentService.CommentServiceImpl;
import jakarta.persistence.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @Column
    private String name;

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

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String path;

    @Column
    private int shares;

    @Column
    private int favorites;


    // Method to generate the name based on profileId and postId
    public void generateName() {
        if (profile != null && id != null) {
            this.name = "post_" + profile.getId() + "_" + id;
        } else{
            logger.error("Failed to rename file before saving it to the file system");
        }
    }

}