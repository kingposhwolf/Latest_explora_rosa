package com.example.demo.Services.CommentService;

import com.example.demo.Dto.CommentDto;

import jakarta.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<Object> saveComment(CommentDto commentDto);

    ResponseEntity<Object> getCommentForPost(@NotNull Long postId);


    ResponseEntity<Object> deleteComment(Long commentId);
}
