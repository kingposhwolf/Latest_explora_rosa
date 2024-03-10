package com.example.demo.Services.CommentService;

import com.example.demo.Dto.CommentDto;
import com.example.demo.Dto.CommentReplyDto;

import jakarta.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<Object> saveComment(CommentDto commentDto);

    ResponseEntity<Object> getCommentForPost(@NotNull Long postId);

    ResponseEntity<Object> replyComment(CommentReplyDto commentReplyDto);

    ResponseEntity<Object> deleteComment(Long commentId);
}
