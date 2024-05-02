package com.example.demo.Services.CommentService;


import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.SocialMedia.Comment.CommentDeleteDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentLikeDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentPostDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentReplyDto;
import com.example.demo.InputDto.SocialMedia.Comment.FetchCommentReplyDto;

public interface CommentService {
    ResponseEntity<Object> saveComment(CommentDto commentDto);

    ResponseEntity<Object> getCommentForPost(CommentPostDto commentPostDto);

    ResponseEntity<Object> replyComment(CommentReplyDto commentReplyDto);

    ResponseEntity<Object> deleteComment(CommentDeleteDto commentDeleteDto);

    ResponseEntity<Object> getCommentReplyForPost(FetchCommentReplyDto fetchCommentReplyDto);

    ResponseEntity<Object> commentLikeOperation(CommentLikeDto commentLikeDto);
}
