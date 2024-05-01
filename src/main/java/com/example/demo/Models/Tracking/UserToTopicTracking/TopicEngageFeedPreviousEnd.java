package com.example.demo.Models.Tracking.UserToTopicTracking;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.UserManagement.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@SQLDelete(sql = "UPDATE topic_engage_feed_previous_end SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class TopicEngageFeedPreviousEnd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false, foreignKey = @ForeignKey(name = "FK_ENGAGE_PROFILE", foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile user;

    @Column(nullable = false)
    private Long previousEnd;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;
}
