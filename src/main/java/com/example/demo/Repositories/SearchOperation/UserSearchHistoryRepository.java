package com.example.demo.Repositories.SearchOperation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.SocialMedia.SearchOperation.UserSearchHistory;

import java.util.Map;
import java.util.Optional;

import com.example.demo.Models.UserManagement.Profile;


@Repository
public interface UserSearchHistoryRepository extends JpaRepository<UserSearchHistory, Long>  {
    Optional<UserSearchHistory> findByProfile(Profile profile);

    @Query("SELECT GROUP_CONCAT(e) as keyword,l.id as id FROM UserSearchHistory l LEFT JOIN l.keywords e WHERE  l.profile.id = :profileId  GROUP BY l.id")
    Optional<Map<String, Object>> findHistoryByProfileId(@Param("profileId") Long profileId);

    @Query("SELECT l.id FROM UserSearchHistory l WHERE  l.profile.id = :profileId")
    Optional<Long> findHistoryIdByProfileId(@Param("profileId") Long profileId);

    @Modifying
    @Query(value = "INSERT INTO search_names (user_search_history_id, keywords) VALUES (:id, :keyword)", nativeQuery = true)
    void addKeyword(@Param("id") Long id, @Param("keyword") String keyword);

    @Modifying
    @Query(value = "INSERT INTO user_search_history (profile_id, deleted) VALUES (:profileId, false)", nativeQuery = true)
    void newHistory(@Param("profileId") Long profileId);

    // @Modifying
    // @Query(value = "INSERT INTO search_names (user_search_history_id, keywords) VALUES (SELECT id FROM user_search_history WHERE user_search_history_id = :profileId, :keyword)", nativeQuery = true)
    // void newKeyword( @Param("keyword") String keyword, @Param("profileId") Long profileId);

    @Modifying
@Query(value = "INSERT INTO search_names (user_search_history_id, keywords) " +
            "SELECT c.id, :keyword " +
            "FROM user_search_history c " +
            "WHERE c.profile_id = :profileId", nativeQuery = true)
void newKeyword(@Param("keyword") String keyword, @Param("profileId") Long profileId);


}
