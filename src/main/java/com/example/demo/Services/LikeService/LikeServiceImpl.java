package com.example.demo.Services.LikeService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Like;
import com.example.demo.Models.Profile;
import com.example.demo.Repositories.LikeRepository;
import com.example.demo.Repositories.ProfileRepository;

import jakarta.validation.constraints.NotNull;

@Service
public class LikeServiceImpl implements LikeService{
    private static final Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);

    private final LikeRepository likeRepository;

    private final ProfileRepository profileRepository;

    public LikeServiceImpl(ProfileRepository profileRepository,LikeRepository likeRepository) {
        this.profileRepository = profileRepository;
        this.likeRepository = likeRepository;
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> saveLike(@NotNull Long profileId) {
        try {
            Optional<Profile> profile = profileRepository.findById(profileId);
            if(!profile.isPresent()){
                logger.error("User profile not found for profile ID: {}", profileId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found");
            }

            Like like = new Like();
            like.setProfile(profile.get());

            Like savedLike = likeRepository.save(like);
            logger.info("Like saved successfully: {}", savedLike);
            return ResponseEntity.status(HttpStatus.CREATED).body("Like created successfully!");
        } catch (Exception e) {
            logger.error("Failed to save comment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @SuppressWarnings("null")
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
