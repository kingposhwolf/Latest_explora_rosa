package com.example.demo.Repositories.SocialMedia.Comment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.SocialMedia.Interactions.CommentLike;
import com.example.demo.Models.SocialMedia.Interactions.Comment;
import com.example.demo.Models.UserManagement.Profile;


public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>{
    
    @Query(value = "SELECT * FROM comment_likes WHERE comment_id = :commentId AND profile_id = :likerId AND deleted = true", nativeQuery = true)
    Optional<CommentLike> findDeletedLikesByPostAndLiker(@Param("commentId") Long commentId, @Param("likerId") Long likerId);

    @Query("SELECT DISTINCT l.liker.id as profileId, l.liker.verificationStatus as verificatioStatus, l.liker.user.username as username, l.liker.user.name as name FROM CommentLike l JOIN l.liker u WHERE l.comment.id = :commentId ")
    List<Map<String, Object>> findLikersListByComment(@Param("commentId") Long commentId);

    Optional<CommentLike> findByCommentAndLiker(Comment comment, Profile liker);

    @Query(value = "SELECT id FROM comment_likes WHERE comment_id = :commentId AND profile_id = :likerId AND deleted = false", nativeQuery = true)
    Optional<Long> findIfLikeComment(@Param("commentId") Long commentId, @Param("likerId") Long likerId);

    @Query(value = "SELECT comment_id FROM comment_likes WHERE profile_id = :likerId AND deleted = false", nativeQuery = true)
    List<Long> commentsUserLike(@Param("likerId") Long likerId);
}
