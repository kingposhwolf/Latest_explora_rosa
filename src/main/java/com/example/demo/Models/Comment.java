package com.example.demo.Models;
/*
 * @author Dwight Danda
 *
 */
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profileId")
    private Profile commenter;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userPostId", nullable = false)
    private UserPost userPost;

}