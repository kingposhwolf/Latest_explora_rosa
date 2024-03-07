package com.example.demo.Repositories;

import com.example.demo.Models.Comment;
import com.example.demo.Models.UserPost;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    
    List<Comment> findByUserPostAndParentCommentIsNull(UserPost userPost);
}
