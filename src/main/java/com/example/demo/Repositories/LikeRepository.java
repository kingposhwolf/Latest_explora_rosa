package com.example.demo.Repositories;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Like;
import com.example.demo.Models.UserManagement.Profile;


public interface LikeRepository extends CrudRepository<Like, Long>{
    Optional<Like> findByLikerAndPost(Profile liker, UserPost post);

    List<Like> findByPost(UserPost post);

    @Query(value = "SELECT * FROM `like` WHERE user_post_id = :postId AND profile_id = :likerId AND deleted = true", nativeQuery = true)
    Optional<Like> findDeletedLikesByPostAndLiker(@Param("postId") Long postId, @Param("likerId") Long likerId);

    @Query(value = "SELECT id FROM `like` WHERE user_post_id = :postId AND profile_id = :likerId AND deleted = false", nativeQuery = true)
    Optional<Long> findIfLikePost(@Param("postId") Long postId, @Param("likerId") Long likerId);

    @Query("SELECT DISTINCT l.liker.id as profileId, l.liker.verificationStatus as verificatioStatus, l.liker.user.username as username, l.liker.user.name as name FROM Like l JOIN l.liker u WHERE l.post.id = :postId ")
    List<Map<String, Object>> findLikersByUsernameAndName(@Param("postId") Long postId);

    @Query("SELECT l.post.id  FROM Like l  WHERE l.liker.id = :profileId ")
    List<Long> postsUserLike(@Param("profileId") Long profileId);

}
