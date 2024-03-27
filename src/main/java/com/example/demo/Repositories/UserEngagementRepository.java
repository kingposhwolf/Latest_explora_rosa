package com.example.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Tracking.UserToUserTracking.UserEngagement;
import com.example.demo.Models.UserManagement.Profile;


public interface UserEngagementRepository extends JpaRepository<UserEngagement, Long>{
    Optional<UserEngagement> findByTargetAndTopic(Profile target, Profile topic);

    List<Profile> findTop4TopicByTargetOrderByScoreDesc(Profile profile);

    @Query("SELECT ue.topic FROM UserEngagement ue WHERE ue.topic IN :topics AND ue.target = :target ORDER BY ue.score DESC")
    List<Profile> findTop3TopicsByTopicInAndTargetOrderByScoreDesc(@Param("topics") List<Profile> topics, @Param("target") Profile target);
}
