package com.example.demo.Services.LikeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.LikeDto;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{
    private static final Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);


    private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "Like";
    private static final String ROUTING_KEY = "likeOperation";


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
}
