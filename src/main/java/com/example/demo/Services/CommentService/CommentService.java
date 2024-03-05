package com.example.demo.Services.CommentService;

import com.example.demo.Dto.CommentDto;
import com.example.demo.Models.Comment;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<Object> writeMessage(CommentDto commentDto);


    ResponseEntity<Object> deleteMessage(CommentDto commentDto);
}
