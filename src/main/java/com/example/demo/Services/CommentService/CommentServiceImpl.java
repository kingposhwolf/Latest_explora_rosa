package com.example.demo.Services.CommentService;

import com.example.demo.InputDto.CommentDto;
import com.example.demo.InputDto.CommentReplyDto;
import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Comment;
import com.example.demo.Repositories.CommentRepository;
import com.example.demo.Repositories.UserPostRepository;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService{
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;

    private final UserPostRepository userPostRepository;

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "Comment";
    private static final String ROUTING_KEY = "commentOperation";
    private static final String REPLY_EXCHANGE_NAME = "CommentReply";
    private static final String REPLY_ROUTING_KEY = "commentReplyOperation";

    @Transactional
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

    @Transactional
    @Override
    public ResponseEntity<Object> replyComment(CommentReplyDto commentReplyDto) {
        try {
            rabbitTemplate.convertAndSend(REPLY_EXCHANGE_NAME, REPLY_ROUTING_KEY, commentReplyDto.toJson());
            
            logger.info("Comment Reply added to queue successfully: ", commentReplyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment Reply created successfully!");
        } catch (Exception e) {
            logger.error("Failed to add comment reply to queue server Error : ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> getCommentForPost(@NotNull Long postId) {
        try {
            Optional<UserPost> post = userPostRepository.findById(postId);
            if (post.isEmpty()) {
                logger.info("Failed to fetch comments, post not found with Id : ", postId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            } else {
                List<Comment> comments = commentRepository.findByUserPostAndParentCommentIsNull(post.get());
                logger.info("Comment Fetch successfully: ", comments);
                return ResponseEntity.status(HttpStatus.OK).body(comments);
            }
        } catch (Exception e) {
            logger.error("Failed to fetch comment to for post server Error : ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @SuppressWarnings("null")
    @Transactional
    @Override
    public ResponseEntity<Object> deleteComment(@NotNull Long commentId) {
        try {
            Optional<Comment> commentOptional = commentRepository.findById(commentId);
            if (commentOptional.isPresent()) {

                commentRepository.deleteById(commentId);
                UserPost post = commentOptional.get().getUserPost();
                int newComments = post.getComments() - 1;

                post.setComments(newComments);
                userPostRepository.save(post);

                messagingTemplate.convertAndSend("/topic/commentCount" + post.getId(),newComments);

                logger.info("Comment deleted successfully");
                return ResponseEntity.status(HttpStatus.OK).body("Comment deleted successfully");
            } else {
                logger.error("Comment not found with ID:", commentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
            }
        } catch (Exception e) {
            logger.error("Failed to delete comment server error : ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
}
