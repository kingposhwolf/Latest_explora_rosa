package com.example.demo.Models;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "`like`")
@SQLDelete(sql = "UPDATE 'like' SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
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

    private boolean deleted = Boolean.FALSE;
}
