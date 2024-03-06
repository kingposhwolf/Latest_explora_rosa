package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "userEngagement")
public class UserEngagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "targetId", nullable = false)
    private Profile target;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "topicId", nullable = false)
    private Profile topic;

    @Column(nullable = false)
    private int score;
}
