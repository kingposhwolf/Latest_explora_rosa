package com.example.demo.Models.SocialMedia;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.UserManagement.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@SQLDelete(sql = "UPDATE mention SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Mention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false, foreignKey = @ForeignKey(name = "FK_MENTION_PROFILE", foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile user;

    @ManyToOne
    @JoinColumn(name = "userPostId", nullable = false, foreignKey = @ForeignKey(name = "FK_POST_MENTION", foreignKeyDefinition = "FOREIGN KEY (user_post_id) REFERENCES user_posts(id) ON DELETE CASCADE"))
    private UserPost post;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;
}
