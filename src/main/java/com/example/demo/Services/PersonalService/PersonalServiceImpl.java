package com.example.demo.Services.PersonalService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.InputDto.ProfileVisitDto;
import com.example.demo.Models.Personal;
import com.example.demo.Repositories.PersonalRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonalServiceImpl implements PersonalService{
    private static final Logger logger = LoggerFactory.getLogger(PersonalServiceImpl.class);
    private final PersonalRepository personalRepository;

   private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "ProfileVisit";
    private static final String ROUTING_KEY = "profileVisitOperation";


    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> getProfileById(ProfileVisitDto profileVisitDto) {
        try {
            Optional<Personal> owner = personalRepository.findById(profileVisitDto.getOwnerId());

            Optional<Personal> visitor = personalRepository.findById(profileVisitDto.getVisitorId());

            if (!owner.isPresent()) {
                logger.error("Failed to Fetch Profile Info, Invalid Profile Id");
                return ResponseEntity.badRequest().body("Invalid profile ID");
            }else if(!visitor.isPresent()){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
             else{
                rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, profileVisitDto.toJson());
                logger.info("\nProfile Info Fetched Successful: " + owner);
                return ResponseEntity.status(200).body(owner.get());
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
