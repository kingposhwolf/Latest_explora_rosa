package com.example.demo.Services.FavoritesService;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.InputDto.FavoritesDto;
import com.example.demo.Models.Favorites;
import com.example.demo.Models.Profile;
import com.example.demo.Models.UserPost;
import com.example.demo.Repositories.FavoritesRepository;
import com.example.demo.Repositories.ProfileRepository;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FavoritesServiceImpl implements FavoritesService{
    private static final Logger logger = LoggerFactory.getLogger(FavoritesServiceImpl.class);

    private final ProfileRepository profileRepository;

    private final FavoritesRepository favoritesRepository;

    private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "Favorites";
    private static final String ROUTING_KEY = "favoritesOperation";

    @Override
    public ResponseEntity<Object> addToFavorites(FavoritesDto favoritesDto) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, favoritesDto.toJson());
            
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
            Optional<Profile> profile = profileRepository.findById(profileId);
            if (profile.isEmpty()) {
                logger.info("Failed to fetch favorites contents, profile not found with Id : ", profileId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
            } else {
                List<UserPost> posts = favoritesRepository.findUserPostsByProfile(profile.get());

                logger.info("Favorites Fetch successfully: ", posts);
                return ResponseEntity.status(HttpStatus.OK).body(posts);
            }
        } catch (Exception e) {
            logger.error("Failed to fetch Favorites Posts server Error : ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @SuppressWarnings("null")
    @Transactional
    @Override
    public ResponseEntity<Object> deleteFavorite(@NotNull Long favoriteId) {
        try {
            Optional<Favorites> favorite = favoritesRepository.findById(favoriteId);
            if (favorite.isPresent()) {

                favoritesRepository.delete(favorite.get());

                logger.info("Favorite deleted successfully");
                return ResponseEntity.status(HttpStatus.OK).body("Favorite deleted successfully");
            } else {
                logger.error("Favorite not found with ID:", favoriteId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favorite not found");
            }
        } catch (Exception e) {
            logger.error("Failed to delete favorite server error : ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
}
