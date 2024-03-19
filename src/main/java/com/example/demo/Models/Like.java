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

    @ManyToOne
    @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "FK_LIKE_PROFILE", foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile liker;

    @ManyToOne
    @JoinColumn(name = "userPostId", nullable = false)
    private UserPost post;
}
