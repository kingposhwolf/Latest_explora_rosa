package com.example.demo.Models.SocialMedia;

import com.example.demo.Models.UserManagement.Profile;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name="stories")
public class Story {

    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @JoinColumn(name = "profileId")
    @ManyToOne(cascade = CascadeType.ALL)
    private Profile profile;

    @Column(nullable = false)
    private String path;

}
