package com.example.demo.Services.FeedsService;

import java.util.Optional;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Components.Helper.Helper;
import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Repositories.FavoritesRepository;
// import com.example.demo.Components.Algorithms.FeedsAlgorithm;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.Repositories.UserPostRepository;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FeedsServiceImpl implements FeedsService{

    private static final Logger logger = LoggerFactory.getLogger(FeedsServiceImpl.class);

    //private final FeedsAlgorithm feedsAlgorithm;

    private final ProfileRepository profileRepository;

    private final UserPostRepository userPostRepository;

    private final FavoritesRepository favoritesRepository;

    private final Helper helper;

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> retrieveFeeds(@NotNull Long profileId) {
        try {
            // Optional<Profile> profileOptional = profileRepository.findById((long) 1);
            // if(profileOptional.isPresent()){
            //     Profile profile = profileOptional.get();
            //     List<UserPost> posts = new ArrayList<>();
            // posts.addAll(feedsAlgorithm.friendPost(profile));
            // posts.addAll(feedsAlgorithm.locationSpecificTrendingPost(profile));
            // posts.addAll(feedsAlgorithm.postBasedOnTopic(profile));
            // posts.addAll(feedsAlgorithm.postBasedOnUserEngagements(profile));
            
            // //posts = ;

            // logger.info("Feeds Fetched Successful ");
            // return ResponseEntity.ok(userPostRepository.findUserPostDataById((long) 1));
            // }else{
            //     logger.error(" Fails to fetch Feeds , Profile Not Found");
            //     return ResponseEntity.status(404).body("Profile Not Found");
            // }
            return ResponseEntity.ok(helper.postMapTimer(userPostRepository.findUserPostData()));
            
        } catch (Exception exception) {
            logger.error("\nFails to fetch Feeds, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> retrieveUserOwnFeeds(@NotNull Long profileId) {
        try {
            Optional<Profile> profileOptional = profileRepository.findById((long) 1);
            if(profileOptional.isPresent()){

            logger.info("Specific User Feeds Fetched Successful ");
            return ResponseEntity.ok(helper.postMapTimer(userPostRepository.findSpecificUserPostData(profileId)));
            }else{
                logger.error(" Fails to fetch Feeds , Profile Not Found");
                return ResponseEntity.status(404).body("Profile Not Found");
            }
        } catch (Exception exception) {
            logger.error("\nFails to fetch Feeds, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> retrieveUserFavoriteFeeds(@NotNull Long profileId) {
        try {
            Optional<Profile> profileOptional = profileRepository.findById((long) 1);
            if(profileOptional.isPresent()){

            logger.info("Specific User Feeds Fetched Successful ");
            return ResponseEntity.ok(helper.postMapTimer(favoritesRepository.findSpecificUserFavoritePost(profileId)));
            }else{
                logger.error(" Fails to fetch Feeds , Profile Not Found");
                return ResponseEntity.status(404).body("Profile Not Found");
            }
        } catch (Exception exception) {
            logger.error("\nFails to fetch Feeds, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    
}
