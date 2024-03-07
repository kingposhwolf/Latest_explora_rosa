package com.example.demo.Controllers.Comment;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Controllers.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.Dto.CommentDto;
import com.example.demo.Services.CommentService.CommentServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentServiceImpl commentService;


    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @Autowired
    private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/new")
    public ResponseEntity<Object> writeMessage(@RequestBody @Valid CommentDto commentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return commentService.saveComment(commentDto);
    }

    @PostMapping("/post")
    public ResponseEntity<Object> getCommentsForPost(@RequestBody @Valid @NotNull Long postId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return commentService.getCommentForPost(postId);
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteMessage(@RequestBody @Valid @NotNull Long commentId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return commentService.deleteComment(commentId);
    }
}
