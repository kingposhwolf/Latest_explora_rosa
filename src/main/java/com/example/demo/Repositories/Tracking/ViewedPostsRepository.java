package com.example.demo.Repositories.Tracking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Tracking.ViewedPosts;

import jakarta.transaction.Transactional;

public interface ViewedPostsRepository extends JpaRepository<ViewedPosts, Long>{
    @Modifying
    @Transactional
    @Query(value = "INSERT IGNORE INTO viewed_posts (profile_id, user_post_id) VALUES (:profileId, :postId)", nativeQuery = true)
    void insertViwedPost(@Param("profileId") Long profileId, @Param("postId") Long postId);

    @Query(value = "SELECT user_post_id FROM viewed_posts WHERE profile_id = :profileId AND deleted = false", nativeQuery = true)
    List<Long> findViewedPostsByProfileId(@Param("profileId") Long profileId);
}
