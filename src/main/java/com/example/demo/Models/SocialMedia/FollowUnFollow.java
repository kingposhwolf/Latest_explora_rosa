package com.example.demo.Models.SocialMedia;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.UserManagement.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE follow_un_follow SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class FollowUnFollow {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "followerId", nullable = false, foreignKey = @ForeignKey(name = "FK_FOLLOWER_PROFILE", foreignKeyDefinition = "FOREIGN KEY (follower_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile follower;

    @ManyToOne
    @JoinColumn(name = "followingId", nullable = false, foreignKey = @ForeignKey(name = "FK_FOLLOWING_PROFILE", foreignKeyDefinition = "FOREIGN KEY (following_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile following;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;
}
