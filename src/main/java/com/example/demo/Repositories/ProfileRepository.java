package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Models.UserManagement.User;

import java.util.List;
import java.util.Map;

import com.example.demo.Models.UserManagement.Management.Status;





public interface ProfileRepository extends CrudRepository<Profile, Long>{
    Profile findByUser(User user);

    @Query("SELECT DISTINCT l.id FROM Profile l WHERE  l.id = :profileId")
    Long findProfileIdById(@Param("profileId") Long profileId);

    @Query("SELECT DISTINCT l FROM Profile l WHERE  l.id = :profileId")
    Optional<Profile> findProfilesById(@Param("profileId") Long profileId);

    Optional<List<Profile>> findByStatus(Status status);

    // @Query(value =
    //     "SELECT p.id as profileId " +
    //     "FROM profiles p " +
    //     "JOIN follow_un_follow f1 ON p.id = f1.following_id " +
    //     "JOIN follow_un_follow f2 ON f1.following_id = f2.follower_id " +
    //     "JOIN users us ON p.user_id = us.id "+
    //     "WHERE f2.following_id = :userId " +
    //     "AND (us.username LIKE CONCAT('%', :keyword, '%') OR us.name LIKE CONCAT('%', :keyword, '%'))",
    //     nativeQuery = true)
    //     List<Map<String, Object>> searchByUserFollowings(@Param("userId") Long userId, @Param("keyword") String keyword);

        @Query(value ="SELECT tp.id as profileId, "+
    "us.username as username, " +
    "us.name as name, "+
    "tp.profile_picture as profilePicture, "+
    "tp.verification_status as verificationStatus, "+
    "usf.username as followedBy "+
"FROM follow_un_follow ue "+
"JOIN profiles tp ON ue.following_id = tp.id "+
"JOIN users us ON tp.user_id = us.id "+
"JOIN follow_un_follow au ON ue.follower_id = au.following_id "+
"JOIN profiles autp ON au.following_id = autp.id "+
"JOIN users usf ON autp.user_id = usf.id "+
"WHERE au.follower_id = :targetId "+
"AND ((LEVENSHTEIN(us.username, :searchText) <= 2 "+
  "OR us.username LIKE CONCAT('%', :searchText, '%')) "+
  "OR (LEVENSHTEIN(us.name, :searchText) <= 2 "+
  "OR us.name LIKE CONCAT('%', :searchText, '%'))) "+
"AND ue.deleted = false", nativeQuery = true)
List<Map<String, Object>> searchByUserFollowings(@Param("targetId") Long targetId, @Param("searchText") String searchText);
}
