package com.example.demo.Models.Tracking.UserToUserTracking;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.UserManagement.Profile;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@SQLDelete(sql = "UPDATE user_engagement SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class UserEngagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "targetId", nullable = false, foreignKey = @ForeignKey(name = "FKpgxij3v3rbmcw9derdloe818l", foreignKeyDefinition = "FOREIGN KEY (target_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile target;

    @ManyToOne
    @JoinColumn(name = "topicId", nullable = false, foreignKey = @ForeignKey(name = "FKpgxij3v3rbmcw9derdloe817l", foreignKeyDefinition = "FOREIGN KEY (topic_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile topic;

    @Column(nullable = false)
    private int score;

    private boolean deleted = Boolean.FALSE;
}
