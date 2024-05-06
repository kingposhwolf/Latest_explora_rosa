package com.example.demo.Services.SearchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Components.Algorithms.SearchAlgorithm;
import com.example.demo.InputDto.SearchDto.SearchDto;
import com.example.demo.Repositories.ProfileRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SearchServiceImpl implements SearchService{
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final ProfileRepository profileRepository;

    private final SearchAlgorithm searchAlgorithm;

    @Override
    public ResponseEntity<Object> suggestiveProfiles(SearchDto searchDto) {
        try {
            Long profile = profileRepository.findProfileIdById(searchDto.getProfileId());

            if(profile == null){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
            else{
                return ResponseEntity.status(200).body(searchAlgorithm.suggestiveProfiles(searchDto.getProfileId(), searchDto.getKeyword()));
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    
}
