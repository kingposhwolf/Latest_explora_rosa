package com.example.demo.Services.FavoritesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Components.Helper.Helper;
import com.example.demo.InputDto.DeleteFavoriteDto;
import com.example.demo.InputDto.FavoritesDto;
import com.example.demo.Repositories.FavoritesRepository;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.Repositories.UserPostRepository;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FavoritesServiceImpl implements FavoritesService{
    private static final Logger logger = LoggerFactory.getLogger(FavoritesServiceImpl.class);

    private final ProfileRepository profileRepository;

    private final FavoritesRepository favoritesRepository;

    private final UserPostRepository userPostRepository;

    private final Helper helper;

    private AmqpTemplate rabbitTemplate;

    private static final String QUEUE_NAME = "saveFavorites";

    @Override
    public ResponseEntity<Object> addToFavorites(FavoritesDto favoritesDto) {
        try {
            rabbitTemplate.convertAndSend(QUEUE_NAME, favoritesDto.toJson());
            
            logger.info("Added to Queue for add to Favorites successfully: ");
            return ResponseEntity.ok("Add to favorites successfully!");
        } catch (Exception e) {
            logger.error("Failed to add to favorites: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> getUserFavorites(@NotNull Long profileId) {
        try {
            Long profile = profileRepository.findProfileIdById(profileId);
            if (profile == null ) {
                logger.info("Failed to fetch favorites contents, profile not found with Id : ", profileId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
            } else {
                List<Long> posts = favoritesRepository.findPostByProfile(profile);

                List<Map<String, Object>> postLists = new ArrayList<>();

                for (Long postId : posts) {
                    Map<String, Object> userPostData = userPostRepository.findUserPostDataById(postId);
                    postLists.add(userPostData);
                }

                logger.info("Favorites Fetch successfully for posts with Ids: ", posts);
                return ResponseEntity.status(HttpStatus.OK).body(helper.mapTimer(postLists));
            }
        } catch (Exception e) {
            logger.error("Failed to fetch Favorites Posts server Error : "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @SuppressWarnings("null")
    @Transactional
    @Override
    public ResponseEntity<Object> deleteFavorite(DeleteFavoriteDto deleteFavoriteDto) {
        try {
            Long favorite = favoritesRepository.findFavoriteByPostAndUser(deleteFavoriteDto.getPostId(),deleteFavoriteDto.getProfileId());
            if (favorite != null) {

                favoritesRepository.deleteById(favorite);

                logger.info("Favorite deleted successfully");
                return ResponseEntity.status(HttpStatus.OK).body("Favorite deleted successfully");
            } else {
                logger.error("Favorite not found with ID : "+ deleteFavoriteDto);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favorite not found");
            }
        } catch (Exception e) {
            logger.error("Failed to delete favorite server error : ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
}
