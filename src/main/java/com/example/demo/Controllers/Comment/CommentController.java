package com.example.demo.Controllers.Comment;
import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.CommentDeleteDto;
import com.example.demo.InputDto.CommentDto;
import com.example.demo.InputDto.CommentPostDto;
import com.example.demo.InputDto.CommentReplyDto;
import com.example.demo.InputDto.FetchCommentReplyDto;
import com.example.demo.Services.CommentService.CommentServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentServiceImpl commentService;


    private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/new")
    public ResponseEntity<Object> newComment(@RequestBody @Valid CommentDto commentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return commentService.saveComment(commentDto);
    }

    @PostMapping("/reply")
    public ResponseEntity<Object> replyComment(@RequestBody @Valid CommentReplyDto commentReplyDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return commentService.replyComment(commentReplyDto);
    }

    @PostMapping("/post")
    public ResponseEntity<Object> getCommentsForPost(@RequestBody @Valid CommentPostDto commentPostDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return commentService.getCommentForPost(commentPostDto.getPostId());
    }

    @PostMapping("/post/reply")
    public ResponseEntity<Object> getCommentsReplyForPost(@RequestBody @Valid FetchCommentReplyDto fetchCommentReplyDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return commentService.getCommentReplyForPost(fetchCommentReplyDto.getParentId(), fetchCommentReplyDto.getPostId());
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteMessage(@RequestBody @Valid CommentDeleteDto commentDeleteDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return commentService.deleteComment(commentDeleteDto);
    }
}
