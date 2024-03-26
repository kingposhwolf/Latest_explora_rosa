package com.example.demo.Models;

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
