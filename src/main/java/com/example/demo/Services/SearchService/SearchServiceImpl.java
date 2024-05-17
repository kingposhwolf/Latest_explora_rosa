package com.example.demo.Services.SearchService;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Components.Algorithms.SearchAlgorithm;
import com.example.demo.Components.Helper.Helper;
import com.example.demo.InputDto.SearchDto.SearchDto;
import com.example.demo.Repositories.SearchOperation.UserSearchHistoryRepository;
import com.example.demo.Repositories.SocialMedia.Content.UserPostRepository;
import com.example.demo.Repositories.UserManagement.AccountManagement.ProfileRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SearchServiceImpl implements SearchService{
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final ProfileRepository profileRepository;

    private AmqpTemplate rabbitTemplate;

    private final SearchAlgorithm searchAlgorithm;

    private final UserSearchHistoryRepository searchHistoryRepository;

    private final UserPostRepository userPostRepository;

    private final Helper helper;
    

    @Override
    public ResponseEntity<Object> suggestiveProfiles(SearchDto searchDto) {
        try {

            Long profile = profileRepository.findProfileIdById(searchDto.getProfileId());

            if(profile == null){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
            else{
                rabbitTemplate.convertAndSend("searchSaveOperation", searchDto.toJson());

                return ResponseEntity.status(200).body(helper.postMapTimer(searchAlgorithm.suggestiveProfiles(searchDto),searchDto.getProfileId()));
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @Override
    public ResponseEntity<Object> fetchSearchHistory(Long profileId) {
        try {
            Long profile = profileRepository.findProfileIdById(profileId);

            if(profile == null){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
            else{
                Optional<Map<String, Object>> historyOpt = searchHistoryRepository.findHistoryByProfileId(profileId);
                Map<String, Object> history = historyOpt.get();
            if (history.get("id") != null) {
                String[] words = ((String) history.get("keyword")).split("\\s*,\\s*");
                
                return ResponseEntity.status(200).body(words);
            } else {
                return ResponseEntity.status(200).body(history);
            }
            }
        } catch (Exception exception) {
            logger.error("\nSearch history fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @Override
    public ResponseEntity<Object> searchResults(SearchDto searchDto) {
        try {
            Long profile = profileRepository.findProfileIdById(searchDto.getProfileId());

            if(profile == null){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
            else{
                // rabbitTemplate.convertAndSend("searchSaveOperation", searchDto.toJson());

                return ResponseEntity.status(200).body(helper.postMapTimer(userPostRepository.searchOnHashTag(searchDto.getKeyword()),searchDto.getProfileId()));
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    // @Override
    // public ResponseEntity<Object> suggestiveProfilesOnFollowings(SearchDto searchDto) {
    //     try {
    //         Long profile = profileRepository.findProfileIdById(searchDto.getProfileId());

    //         if(profile == null){
    //             logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
    //             return ResponseEntity.badRequest().body("Your profile ID is Invalid");
    //         }
    //         else{
    //             return ResponseEntity.status(200).body(searchAlgorithm.searchOnCountryFame(searchDto.getCountryId(), searchDto.getKeyword()));
    //         }
    //     } catch (Exception exception) {
    //         logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
    //         return ResponseEntity.status(500).body("Internal Server Error");
    //     }
    // }
    
}
