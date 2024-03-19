package com.example.demo.Models;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@SQLDelete(sql = "UPDATE follow_un_follow SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class FollowUnFollow {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "followerId", nullable = false)
    private Profile follower;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "followingId", nullable = false)
    private Profile following;

    private boolean deleted = Boolean.FALSE;
}
