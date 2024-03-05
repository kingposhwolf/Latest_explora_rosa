package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.access.method.P;

import java.time.LocalDateTime;
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

    @Column
    private String thumbnail;

    @Column
    private LocalDateTime time;

    @Column
    private String type;

    @Column
    private String path;

}
