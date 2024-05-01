package com.example.demo.Services.PersonalService;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.InputDto.UserManagement.Profile.GetProfileDto;
import com.example.demo.InputDto.UserManagement.Profile.ProfileVisitDto;
import com.example.demo.Repositories.PersonalRepository;
import com.example.demo.Repositories.ProfileRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonalServiceImpl implements PersonalService{

    private static final Logger logger = LoggerFactory.getLogger(PersonalServiceImpl.class);

    private final PersonalRepository personalRepository;

    private final ProfileRepository profileRepository;

   private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "ProfileVisit";
    private static final String ROUTING_KEY = "profileVisitOperation";


    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> getProfileById(ProfileVisitDto profileVisitDto) {
        try {
            Map<String, Object> owner = personalRepository.findProfileInfoById(profileVisitDto.getOwnerId());

            Long visitor = profileRepository.findProfileIdById(profileVisitDto.getVisitorId());

            if (owner.size() == 0) {
                logger.error("Failed to Fetch Profile Info, Invalid Profile Id");
                return ResponseEntity.badRequest().body("Invalid profile ID");
            }else if(visitor == null){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
            else{
                rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, profileVisitDto.toJson());
                logger.info("\nProfile Info Fetched Successful: " + owner);
                return ResponseEntity.status(200).body(owner);
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> getOwnProfileById(GetProfileDto getProfileDto) {
        try {
            Map<String, Object> profile = personalRepository.findProfileInfoById(getProfileDto.getId());

            if (profile.size() == 0) {
                logger.error("Failed to Fetch Profile Info, Invalid Profile Id");
                return ResponseEntity.badRequest().body("Invalid profile ID");
            }else{
                logger.info("\nProfile Info Fetched Successful: " + profile);
                return ResponseEntity.status(200).body(profile);
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
