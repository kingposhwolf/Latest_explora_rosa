package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "`like`")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profileId", nullable = false)
    private Profile liker;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userPostId", nullable = false)
    private UserPost post;
}
