package com.example.demo.Services.CommentService;

import jakarta.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.CommentDto;
import com.example.demo.InputDto.CommentReplyDto;

public interface CommentService {
    ResponseEntity<Object> saveComment(CommentDto commentDto);

    ResponseEntity<Object> getCommentForPost(@NotNull Long postId);

    ResponseEntity<Object> replyComment(CommentReplyDto commentReplyDto);

    ResponseEntity<Object> deleteComment(Long commentId);

    ResponseEntity<Object> getCommentReplyForPost(@NotNull Long parentId);
}
