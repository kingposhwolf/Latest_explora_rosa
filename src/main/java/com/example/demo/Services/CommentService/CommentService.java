package com.example.demo.Services.CommentService;

import jakarta.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.SocialMedia.Comment.CommentDeleteDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentLikeDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentReplyDto;

public interface CommentService {
    ResponseEntity<Object> saveComment(CommentDto commentDto);

    ResponseEntity<Object> getCommentForPost(@NotNull Long postId);

    ResponseEntity<Object> replyComment(CommentReplyDto commentReplyDto);

    ResponseEntity<Object> deleteComment(CommentDeleteDto commentDeleteDto);

    ResponseEntity<Object> getCommentReplyForPost(@NotNull Long parentId, @NotNull Long postId);

    ResponseEntity<Object> commentLikeOperation(CommentLikeDto commentLikeDto);
}
