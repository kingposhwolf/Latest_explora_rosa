package com.example.demo.Models.Tracking.UserToTopicTracking;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.SocialMedia.HashTag;
import com.example.demo.Models.UserManagement.Profile;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "topicEngagements")
@SQLDelete(sql = "UPDATE topic_engagements SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class TopicEngagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false, foreignKey = @ForeignKey(name = "FK_TOPIC_ENGAGE_PROFILE", foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "hashTagId", nullable = false, foreignKey = @ForeignKey(name = "FK_SKHRF6482J_PROFILE", foreignKeyDefinition = "FOREIGN KEY (hash_tag_id) REFERENCES hash_tags(id) ON DELETE CASCADE"))
    private HashTag hashTags;

    @Column(nullable = false)
    private int score;

    private boolean deleted = Boolean.FALSE;
}
