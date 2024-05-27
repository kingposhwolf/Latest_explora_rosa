package com.example.demo.Models.Tracking;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.SocialMedia.UserPost;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE viewed_posts SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class ViewedPosts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userPostId", nullable = false, foreignKey = @ForeignKey(name = "FK_POST_VIEWED", foreignKeyDefinition = "FOREIGN KEY (user_post_id) REFERENCES user_posts(id) ON DELETE CASCADE"))
    private UserPost post;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;
}
