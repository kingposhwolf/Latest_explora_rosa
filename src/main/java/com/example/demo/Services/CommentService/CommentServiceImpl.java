package com.example.demo.Services.CommentService;

import com.example.demo.Dto.CommentDto;
import com.example.demo.Models.Comment;
import com.example.demo.Repositories.CommentRepository;

import jakarta.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "Comment";
    private static final String ROUTING_KEY = "commentOperation";

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    @Transactional
    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> saveComment(CommentDto commentDto) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, commentDto.toJson());
            
            logger.info("Comment added to queue successfully: ", commentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully!");
        } catch (Exception e) {
            logger.error("Failed to add comment to queue server Error : ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> deleteComment(@NotNull Long commentId) {
        try {
            // Check if the comment exists
            Optional<Comment> commentOptional = commentRepository.findById(commentId);
            if (commentOptional.isPresent()) {
                // Delete the comment
                commentRepository.deleteById(commentId);
                logger.info("Comment deleted successfully");
                return ResponseEntity.status(HttpStatus.OK).body("Comment deleted successfully");
            } else {
                logger.error("Comment not found with ID: {}", commentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
            }
        } catch (Exception e) {
            logger.error("Failed to delete comment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete comment");
        }
    }
}
