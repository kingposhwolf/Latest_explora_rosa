package com.example.demo.Repositories;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.SocialMedia.FollowUnFollow;
import com.example.demo.Models.UserManagement.Profile;


public interface FollowUnFollowRepository extends JpaRepository<FollowUnFollow, Long>{
    List<Profile> findFollowerByFollowing(Profile profile);

    List<Profile> findFollowingByFollower(Profile profile);

    @Query(value = "SELECT * FROM follow_un_follow WHERE following_id = :followingId AND follower_id = :followerId AND deleted = true", nativeQuery = true)
    Optional<FollowUnFollow> findDeletedEngagement(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    @Query("SELECT cr.id FROM FollowUnFollow cr WHERE  cr.following.id = :followingId AND cr.follower.id = :followerId")
    Optional<Long> findEngangeId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    @Query("SELECT cr.follower.id as profileId, cr.follower.user.username as username, cr.follower.user.name as name, cr.follower.verificationStatus as verificationStatus, cr.follower.profilePicture as profilePicture FROM FollowUnFollow cr WHERE  cr.following.id = :profileId")
    Optional<List<Map<String, Object>>> findFollowers(@Param("profileId") Long profileId);

    @Query("SELECT cr.following.id as profileId, cr.following.user.username as username, cr.following.user.name as name, cr.following.verificationStatus as verificationStatus, cr.following.profilePicture as profilePicture FROM FollowUnFollow cr WHERE  cr.follower.id = :profileId")
    Optional<List<Map<String, Object>>> findFollowings(@Param("profileId") Long profileId);


    @Query(value ="SELECT tp.id as profileId, "+
    "us.username as username, " +
    "us.name as name, "+
    "tp.profile_picture as profilePicture, "+
    "tp.verification_status as verificationStatus "+
"FROM follow_un_follow ue "+
"JOIN profiles tp ON ue.following_id = tp.id "+
"JOIN users us ON tp.user_id = us.id "+
"WHERE ue.follower_id = :targetId "+
"AND ((LEVENSHTEIN(us.username, :searchText) <= 2 "+
  "OR us.username LIKE CONCAT(:searchText, '%')) "+
  "OR (LEVENSHTEIN(us.name, :searchText) <= 2 "+
  "OR us.name LIKE CONCAT(:searchText, '%'))) "+
"AND ue.deleted = false", nativeQuery = true)
HashSet<Map<String, Object>> searchOnFollowing(@Param("targetId") Long targetId, @Param("searchText") String searchText);
}
