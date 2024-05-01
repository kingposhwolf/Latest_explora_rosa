package com.example.demo.Services.FollowUnFollowService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.InputDto.SocialMedia.FollowUnFollow.FollowUnFollowDto;
import com.example.demo.Repositories.FollowUnFollowRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FollowUnFollowServiceImpl implements FollowUnFollowService{
    private static final Logger logger = LoggerFactory.getLogger(FollowUnFollowServiceImpl.class);

    private final AmqpTemplate rabbitTemplate;

    private final FollowUnFollowRepository followUnFollowRepository;

    @Override
    public ResponseEntity<Object> followOperation(FollowUnFollowDto followUnFollowDto) {
        try {
            rabbitTemplate.convertAndSend("followOperation", followUnFollowDto.toJson());
            
            logger.info("Follow Operation successfully: ");
            return ResponseEntity.ok("Follow successfully!");
        } catch (AmqpException e) {
        logger.error("Failed to send message to RabbitMQ"+ e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message to RabbitMQ");
        }catch (Exception exception) {
            logger.error("Failed to follow, server Error : "+ exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @Override
    public ResponseEntity<Object> fetchFollowers(Long profileId) {
        try {
            Optional<List<Map<String, Object>>> followers = followUnFollowRepository.findFollowers(profileId);
            if(followers.isPresent()){
                logger.info("fetch followers successfully");
                return ResponseEntity.ok(followers.get());
            }else{
                logger.info("fetch followers successfully");
                return ResponseEntity.ok("there is no followers");
            }
        }catch (Exception exception) {
            logger.error("Failed to fetch followers, server Error : "+ exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @Override
    public ResponseEntity<Object> fetchFollowing(Long profileId) {
        try {
            Optional<List<Map<String, Object>>> followings = followUnFollowRepository.findFollowings(profileId);
            if(followings.isPresent()){
                logger.info("fetch followings successfully");
                return ResponseEntity.ok(followings.get());
            }else{
                logger.info("fetch followings successfully");
                return ResponseEntity.ok("there is no followers");
            }
        }catch (Exception exception) {
            logger.error("Failed to fetch following, server Error : "+ exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
    
}
