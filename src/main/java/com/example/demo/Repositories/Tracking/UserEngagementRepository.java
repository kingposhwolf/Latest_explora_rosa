package com.example.demo.Repositories.Tracking;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Tracking.UserToUserTracking.UserEngagement;
import com.example.demo.Models.UserManagement.Profile;

@Repository
public interface UserEngagementRepository extends JpaRepository<UserEngagement, Long>{
    Optional<UserEngagement> findByTargetAndTopic(Profile target, Profile topic);

    List<Profile> findTop4TopicByTargetOrderByScoreDesc(Profile profile);

    @Query("SELECT ue.topic FROM UserEngagement ue WHERE ue.topic IN :topics AND ue.target = :target ORDER BY ue.score DESC")
    List<Profile> findTop3TopicsByTopicInAndTargetOrderByScoreDesc(@Param("topics") List<Profile> topics, @Param("target") Profile target);

    @Query(value ="SELECT tp.id as profileId, "+
    "us.username as username, " +
    "us.name as name, "+
    "tp.profile_picture as profilePicture, "+
    "tp.verification_status as verificationStatus "+
    "FROM user_engagement ue "+
    "JOIN profiles tp ON ue.topic_id = tp.id "+
    "JOIN users us ON tp.user_id = us.id "+
    "WHERE ue.target_id = :targetId "+
    "AND ((LEVENSHTEIN(us.username, :searchText) <= 2 "+
    "OR us.username LIKE CONCAT('%', :searchText, '%')) "+
    "OR (LEVENSHTEIN(us.name, :searchText) <= 2 "+
    "OR us.name LIKE CONCAT('%', :searchText, '%'))) "+
    "AND ue.deleted = false", nativeQuery = true)
    List<Map<String, Object>> searchByTargetAndTopic(@Param("targetId") Long targetId, @Param("searchText") String searchText);

    @Query(value = "SELECT te.topic_id " +
                  "FROM topic_engagement te " +
                  "WHERE te.target_id = :targetProfileId " +
                  "AND e.topic_id NOT IN (:excludedTopicIds) " +
                  "ORDER BY te.score DESC " +
                  "LIMIT 10", 
          nativeQuery = true)
    List<Long> findEngagedUsers(@Param("targetProfileId") Long targetProfileId, @Param("excludedTopicIds") List<Long> excludedTopicIds);

}
