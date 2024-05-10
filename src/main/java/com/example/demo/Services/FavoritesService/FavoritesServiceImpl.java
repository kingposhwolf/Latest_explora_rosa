package com.example.demo.Services.FavoritesService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.InputDto.SocialMedia.Favorites.DeleteFavoriteDto;
import com.example.demo.InputDto.SocialMedia.Favorites.FavoritesDto;
import com.example.demo.Repositories.SocialMedia.Favorite.FavoritesRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FavoritesServiceImpl implements FavoritesService{
    private static final Logger logger = LoggerFactory.getLogger(FavoritesServiceImpl.class);


    private final FavoritesRepository favoritesRepository;

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
    @Transactional
    @Override
    public ResponseEntity<Object> deleteFavorite(DeleteFavoriteDto deleteFavoriteDto) {
        try {
            Optional<Long> favorite = favoritesRepository.findFavoriteByPostAndUser(deleteFavoriteDto.getPostId(),deleteFavoriteDto.getProfileId());
            if (favorite.isPresent()) {

                favoritesRepository.deleteById(favorite.get());

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
