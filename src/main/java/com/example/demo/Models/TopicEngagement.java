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

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "profileId", nullable = false, columnDefinition = "BIGINT NOT NULL REFERENCES profiles(id) ON DELETE CASCADE")
    private Profile profile;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hashTagId", nullable = false)
    private HashTag hashTags;

    @Column(nullable = false)
    private int score;
}
