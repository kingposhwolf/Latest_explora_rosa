package com.example.demo.Services.CommentService;

import com.example.demo.Dto.CommentDto;
import com.example.demo.Models.Comment;
import com.example.demo.Models.Profile;
import com.example.demo.Repositories.CommentRepository;
import com.example.demo.Repositories.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;

    private final ProfileRepository profileRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ProfileRepository profileRepository) {
        this.commentRepository = commentRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public ResponseEntity<Object> writeMessage(CommentDto commentDto) {
        try {
            Optional<Profile> profile = profileRepository.findById(commentDto.getProfileId());
            if(!profile.isPresent()){
                logger.error("User profile not found for profile ID: {}", commentDto.getProfileId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found");
            }

            // Create a new comment and set its message
            Comment comment = new Comment();
            comment.setMessage(commentDto.getMessage());

            // Link the comment to the user's profile
            comment.setProfile(profile.get());

            // Save the comment to the database
            Comment savedComment = commentRepository.save(comment);
            logger.info("Comment saved successfully: {}", savedComment);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully!");
        } catch (Exception e) {
            logger.error("Failed to save comment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save comment");
        }
    }

    @Override
    public ResponseEntity<Object> deleteMessage(CommentDto commentDto) {
        try {
            // Check if the comment exists
            Optional<Comment> commentOptional = commentRepository.findById(commentDto.getId());
            if (commentOptional.isPresent()) {
                // Delete the comment
                commentRepository.deleteById(commentDto.getId());
                logger.info("Comment deleted successfully");
                return ResponseEntity.status(HttpStatus.OK).body("Comment deleted successfully");
            } else {
                logger.error("Comment not found with ID: {}", commentDto.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
            }
        } catch (Exception e) {
            logger.error("Failed to delete comment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete comment");
        }
    }

}
