package com.example.demo.Services.LikeService;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.InputDto.LikeDto;
import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Like;
// import com.example.demo.Repositories.CountryRepository;
import com.example.demo.Repositories.LikeRepository;
import com.example.demo.Repositories.UserPostRepository;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService{
    private static final Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);

    private final UserPostRepository userPostRepository;

    //private final CountryRepository countryRepository;


    @Autowired
    private final LikeRepository likeRepository;

    private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "Like";
    private static final String ROUTING_KEY = "likeOperation";


    @Override
    public ResponseEntity<Object> likeOperation(LikeDto likeDto) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, likeDto.toJson());
          // countryRepository.deleteById(likeDto.getLikerId());
            
            logger.info("Like saved successfully: ");
            return ResponseEntity.ok("Like successfully!");
        } catch (AmqpException e) {
        logger.error("Failed to send message to RabbitMQ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message to RabbitMQ");
        }catch (Exception e) {
            logger.error("Failed to like server Error : ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }


    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> fetchLikes(@NotNull Long postId) {
        try {
            Optional<UserPost> post = userPostRepository.findById(postId);
            if (post.isEmpty()) {
                logger.error("Failed to fetch Likes Post not found for postId: ", postId);
                return ResponseEntity.status(404).body("Can't retrieve likes , Post not Find");
            }else{
                List<Like> postLikes = likeRepository.findByPost(post.get());
                logger.info("\nSuccessful Fetch the likes which are : " + postLikes);
                return ResponseEntity.status(200).body(postLikes);
            }
        } catch (Exception exception) {
            logger.error("Failed to fetch Likes server error: ", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
}
