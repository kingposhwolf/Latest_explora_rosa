package com.example.demo.Models.SocialMedia.Interactions;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.UserManagement.Profile;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "`like`")
@SQLDelete(sql = "UPDATE `like` SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "FK_LIKE_8761046PROFILE", foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile liker;

    @ManyToOne
    @JoinColumn(name = "userPostId", nullable = false, foreignKey = @ForeignKey(name = "FK_POST_LIKE", foreignKeyDefinition = "FOREIGN KEY (user_post_id) REFERENCES user_posts(id) ON DELETE CASCADE"))
    @ToString.Exclude
    private UserPost post;

    private boolean deleted = Boolean.FALSE;
}
