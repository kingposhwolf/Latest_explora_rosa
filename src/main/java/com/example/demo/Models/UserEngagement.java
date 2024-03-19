package com.example.demo.Models;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "userEngagement")
@SQLDelete(sql = "UPDATE user_engagement SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class UserEngagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "targetId", nullable = false, foreignKey = @ForeignKey(name = "FK_TARGET_ENGAGE_PROFILE", foreignKeyDefinition = "FOREIGN KEY (target_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile target;

    @ManyToOne
    @JoinColumn(name = "topicId", nullable = false, foreignKey = @ForeignKey(name = "FK_TOPIC_ENGAGE_PROFILE", foreignKeyDefinition = "FOREIGN KEY (topic_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile topic;

    @Column(nullable = false)
    private int score;

    private boolean deleted = Boolean.FALSE;
}
