package com.example.demo.Services.CommentService;

import com.example.demo.Dto.CommentDto;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<Object> saveComment(CommentDto commentDto);


    ResponseEntity<Object> deleteComment(Long commentId);
}
