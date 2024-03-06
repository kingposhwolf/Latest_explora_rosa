package com.example.demo.Models;
/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="userPosts")
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
    @JoinTable(name = "userPostHashTag",joinColumns = @JoinColumn(name = "userPostId"),inverseJoinColumns = @JoinColumn(name = "hashTagId", nullable = false))
    private List<HashTag> hashTags;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brandId")
    private Brand brand;

    @OneToMany(mappedBy = "userPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @Column
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

}
