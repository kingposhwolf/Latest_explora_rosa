package com.example.demo.Services.BrandService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.InputDto.UserManagement.Profile.GetProfileDto;
import com.example.demo.InputDto.UserManagement.Profile.ProfileVisitDto;
import com.example.demo.Repositories.BrandRepository;
import com.example.demo.Repositories.FollowUnFollowRepository;
import com.example.demo.Repositories.ProfileRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService{

    private static final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    private final BrandRepository brandRepository;

    private final ProfileRepository profileRepository;

    private final FollowUnFollowRepository followUnFollowRepository;

    private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "ProfileVisit";
    private static final String ROUTING_KEY = "profileVisitOperation";

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> getBrandById(ProfileVisitDto brandVisitDto) {

        try {
            Map<String, Object> owner = brandRepository.findProfileInfoById(brandVisitDto.getOwnerId());

            Long visitor = profileRepository.findProfileIdById(brandVisitDto.getVisitorId());

            if (owner.size() == 0) {
                logger.error("Failed to Fetch Brand Info, Invalid brand Id");
                return ResponseEntity.badRequest().body("Invalid profile ID");
            }else if(visitor == null){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
            else{
                Optional<Long> followProfile = followUnFollowRepository.findEngangeId(brandVisitDto.getVisitorId(), brandVisitDto.getOwnerId());

                Map<String, Object> modifiedProfile = new HashMap<>(owner);
            
                if (followProfile.isPresent()) {
                    modifiedProfile.put("followProfile", true);
                }else{
                    modifiedProfile.put("followProfile", false);
                }
                rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, brandVisitDto.toJson());
                logger.info("\nProfile Info Fetched Successful: " + modifiedProfile);
                return ResponseEntity.status(200).body(modifiedProfile);
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> getOwnBrandById(GetProfileDto getProfileDto) {

        try {
            Map<String, Object> profile = brandRepository.findProfileInfoById(getProfileDto.getId());

            if (profile.size() == 0) {
                logger.error("Failed to Fetch Brand Info, Invalid brand Id");
                return ResponseEntity.badRequest().body("Invalid profile ID");
            }
            else{
                logger.info("\nProfile Info Fetched Successful: " + profile);
                return ResponseEntity.status(200).body(profile);
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    
}
