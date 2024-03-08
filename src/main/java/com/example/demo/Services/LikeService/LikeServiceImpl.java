package com.example.demo.Services.LikeService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.LikeDto;
import com.example.demo.Models.Like;
import com.example.demo.Repositories.LikeRepository;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{
    private static final Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);

    private final LikeRepository likeRepository;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "Like";
    private static final String ROUTING_KEY = "likeOperation";

    @Transactional
    @Override
    public ResponseEntity<Object> likeOperation(LikeDto likeDto) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, likeDto.toJson());
            
            logger.info("Like saved successfully: {}");
            return ResponseEntity.ok("Like successfully!");
        } catch (Exception e) {
            logger.error("Failed to save comment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @SuppressWarnings("null")
    @Transactional
    @Override
    public ResponseEntity<Object> deleteLike(@NotNull Long likeId) {
        try {
            
            Optional<Like> like = likeRepository.findById(likeId);
            if (like.isPresent()) {
            
                likeRepository.deleteById(likeId);
                logger.info("Like deleted successfully");
                return ResponseEntity.status(HttpStatus.OK).body("unlike successfully");
            } else {
                logger.error("Like not found with ID: {}", likeId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Like not found");
            }
        } catch (Exception e) {
            logger.error("Failed to delete Like , Server Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
}
