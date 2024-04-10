package com.example.demo.Models.SocialMedia.Interactions;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.UserManagement.Profile;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "commentLikes")
@SQLDelete(sql = "UPDATE comment_likes SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "FK_LIKE_PROFILE", foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile liker;

    @ManyToOne
    @JoinColumn(name = "commentId", nullable = false, foreignKey = @ForeignKey(name = "FK_COMMENT_LIKE", foreignKeyDefinition = "FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE"))
    @ToString.Exclude
    private Comment comment;

    private boolean deleted = Boolean.FALSE;
}
