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

    @Query("SELECT DISTINCT l.commenter.id as profileId, l.commenter.verificationStatus as verificationStatus, l.commenter.user.username as username, l.commenter.user.name as name, l.message as message, l.id as id, l.timestamp as timestamp  FROM Comment l JOIN l.commenter WHERE l.userPost.id = :postId AND l.parentComment IS NULL")
List<Map<String, Object>> findCommentsForPost(@Param("postId") Long postId);

@Query("SELECT DISTINCT l.commenter.id as profileId, l.commenter.verificationStatus as verificationStatus, l.commenter.user.username as username, l.commenter.user.name as name, l.message as message, l.id as id, l.timestamp as timestamp  FROM Comment l JOIN l.commenter WHERE  l.parentComment.id = :parentId")
List<Map<String, Object>> findCommentsReply(@Param("parentId") Long parentId);



}
