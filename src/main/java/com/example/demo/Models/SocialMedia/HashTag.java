package com.example.demo.Models.SocialMedia;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.Tracking.UserToTopicTracking.TopicEngagement;
import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "hashTags")
@SQLDelete(sql = "UPDATE hashTags SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;

    //Below is For Bidirection relationship
    @OneToMany(mappedBy = "hashTags", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TopicEngagement> topicEngagementList = new ArrayList<>();
}
