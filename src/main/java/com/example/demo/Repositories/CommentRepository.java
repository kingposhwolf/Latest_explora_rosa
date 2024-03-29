package com.example.demo.Repositories;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    
    List<Comment> findByUserPostAndParentCommentIsNull(UserPost userPost);
}
