package com.example.demo.Models.SocialMedia.Interactions;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.UserManagement.Profile;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@SQLDelete(sql = "UPDATE tag SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false, foreignKey = @ForeignKey(name = "FK_TAG_PROFILE", foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile user;

    @ManyToOne
    @JoinColumn(name = "userPostId", nullable = false, foreignKey = @ForeignKey(name = "FK_POST_TAG", foreignKeyDefinition = "FOREIGN KEY (user_post_id) REFERENCES user_posts(id) ON DELETE CASCADE"))
    private UserPost post;

    private boolean deleted = Boolean.FALSE;
}
