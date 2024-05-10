package com.example.demo.Services.CommentService;

import com.example.demo.Components.Helper.Helper;
import com.example.demo.InputDto.SocialMedia.Comment.CommentDeleteDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentLikeDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentPostDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentReplyDto;
import com.example.demo.InputDto.SocialMedia.Comment.FetchCommentReplyDto;
import com.example.demo.Repositories.SocialMedia.Comment.CommentRepository;
import com.example.demo.Repositories.SocialMedia.Content.UserPostRepository;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService{
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;

    private final UserPostRepository userPostRepository;

    private final Helper helper;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "Comment";
    private static final String ROUTING_KEY = "commentOperation";
    private static final String REPLY_EXCHANGE_NAME = "CommentReply";
    private static final String REPLY_ROUTING_KEY = "commentReplyOperation";

    @Override
    public ResponseEntity<Object> saveComment(CommentDto commentDto) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, commentDto.toJson());
            
            logger.info("Comment added to queue successfully: ", commentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully!");
        } catch (Exception e) {
            logger.error("Failed to add comment to queue server Error : "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @Override
    public ResponseEntity<Object> replyComment(CommentReplyDto commentReplyDto) {
        try {
            rabbitTemplate.convertAndSend(REPLY_EXCHANGE_NAME, REPLY_ROUTING_KEY, commentReplyDto.toJson());
            
            logger.info("Comment Reply added to queue successfully: ", commentReplyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment Reply created successfully!");
        } catch (Exception e) {
            logger.error("Failed to add comment reply to queue server Error : "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @Override
    public ResponseEntity<Object> getCommentForPost(CommentPostDto commentPostDto) {
        try {
            Optional<Long> post = userPostRepository.findPostIdByItsId(commentPostDto.getPostId());
            if (post.isEmpty()) {
                logger.info("Failed to fetch comments, post not found with Id : ", commentPostDto.getPostId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            } else {
                logger.info("Comment Fetch successfully: ");
                return ResponseEntity.status(HttpStatus.OK).body(helper.mapCommentTimer(commentRepository.findCommentsForPost(post.get()), commentPostDto.getProfileId()));
            }
        } catch (Exception e) {
            logger.error("Failed to fetch comment to for post server Error : "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @Override
    public ResponseEntity<Object> getCommentReplyForPost(FetchCommentReplyDto fetchCommentReplyDto) {
        try {
            List<Map<String, Object>> replies = commentRepository.findCommentsReply(fetchCommentReplyDto.getParentId(),fetchCommentReplyDto.getPostId());
            if(replies.size() == 0){
                logger.error("Failed to fetch comment replyt for the parent : " + fetchCommentReplyDto.getParentId());
                return ResponseEntity.status(404).body("No reply found");
            }else{
                logger.info("Comment Reply Fetched successfully");
                return ResponseEntity.status(HttpStatus.OK).body(helper.mapCommentTimer(replies, fetchCommentReplyDto.getProfileId()));
            }
        } catch (Exception e) {
            logger.error("Failed to fetch comment to for post server Error : "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteComment(CommentDeleteDto commentDeleteDto) {
        try {
            if(commentDeleteDto.isOwnPost()){
                Map<String, Object> comment = commentRepository.findCommentByIdAndPoster(commentDeleteDto.getCommentId(),commentDeleteDto.getCommenterOrPosterId());

                if(comment.size() != 0){
                    commentRepository.deleteById(commentDeleteDto.getCommentId());
                    return ResponseEntity.status(HttpStatus.OK).body("Comment Deleted successful");
                }else{
                    return ResponseEntity.status(404).body("Comment Does not Found");
                }
            }else{
                Map<String, Object> comment = commentRepository.findCommentByIdAndCommenter(commentDeleteDto.getCommentId(),commentDeleteDto.getCommenterOrPosterId());
                if(comment.size() != 0){
                    commentRepository.deleteById(commentDeleteDto.getCommentId());
                    return ResponseEntity.status(HttpStatus.OK).body("Comment Deleted successful");
                }else{
                    return ResponseEntity.status(404).body("Comment Does not Found");
                }
            }
        } catch (Exception e) {
            logger.error("Failed to delete comment server error : "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @Override
    public ResponseEntity<Object> commentLikeOperation(CommentLikeDto commentLikeDto) {
        try {
            rabbitTemplate.convertAndSend("likeComment", commentLikeDto.toJson());
            
            logger.info("Comment Like operation successfully: ");
            return ResponseEntity.ok("Comment Like successfully!");
        } catch (AmqpException e) {
        logger.error("Failed to send message to RabbitMQ: "+ e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message to RabbitMQ");
        }catch (Exception e) {
            logger.error("Failed to like comment, server Error : "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
}
