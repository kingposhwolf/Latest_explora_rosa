package com.example.demo.Repositories;


import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    
    List<Comment> findByUserPostAndParentCommentIsNull(UserPost userPost);

    @Query("SELECT DISTINCT l.commenter.id as profileId, l.commenter.verificationStatus as verificationStatus, l.commenter.user.username as username, l.commenter.user.name as name, l.message as message, l.id as id, l.timestamp as timestamp  FROM Comment l WHERE l.userPost.id = :postId AND l.parentComment IS NULL")
List<Map<String, Object>> findCommentsForPost(@Param("postId") Long postId);

@Query("SELECT DISTINCT l.commenter.id as profileId, l.commenter.verificationStatus as verificationStatus, l.commenter.user.username as username, l.commenter.user.name as name, l.message as message, l.id as id, l.timestamp as timestamp,l.parentComment.id as parentComment  FROM Comment l WHERE  l.parentComment.id = :parentId AND l.userPost.id = :postId")
List<Map<String, Object>> findCommentsReply(@Param("parentId") Long parentId, @Param("postId") Long postId);

@Query("SELECT DISTINCT l.id as id, l.parentComment.id as parentComment FROM Comment l WHERE  l.id = :commentId AND l.userPost.profile.id = :profileId")
Map<String, Object> findCommentByIdAndPoster(@Param("commentId") Long commentId, @Param("profileId") Long profileId);

@Query("SELECT DISTINCT l.id as id, l.parentComment.id as parentComment FROM Comment l WHERE  l.id = :commentId AND l.commenter.id = :profileId")
Map<String, Object> findCommentByIdAndCommenter(@Param("commentId") Long commentId, @Param("profileId") Long profileId);

}
