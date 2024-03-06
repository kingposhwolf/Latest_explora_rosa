package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "topicEngagements")
public class TopicEngagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profileId", nullable = false)
    private Profile profile;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hashTagId", nullable = false)
    private HashTag hashTags;

    @Column(nullable = false)
    private int score;
}
