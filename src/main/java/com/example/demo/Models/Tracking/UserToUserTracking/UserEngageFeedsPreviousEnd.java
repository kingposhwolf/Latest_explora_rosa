package com.example.demo.Models.Tracking.UserToUserTracking;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.UserManagement.Profile;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@SQLDelete(sql = "UPDATE user_engage_feeds_previous_end SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class UserEngageFeedsPreviousEnd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "profileId", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_ENGAGE_PROFILE", foreignKeyDefinition = "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile user;

    @Column(nullable = false)
    private Long previousEnd;

    private boolean deleted = Boolean.FALSE;
}
