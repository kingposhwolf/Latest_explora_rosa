package com.example.demo.Services.BrandService;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.InputDto.ProfileVisitDto;
import com.example.demo.Repositories.BrandRepository;
import com.example.demo.Repositories.ProfileRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService{

    private static final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    private final BrandRepository brandRepository;

    private final ProfileRepository profileRepository;

    private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "ProfileVisit";
    private static final String ROUTING_KEY = "profileVisitOperation";

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> getBrandById(ProfileVisitDto brandVisitDto) {

        try {
            Map<String, Object> owner = brandRepository.findProfileInfoById(brandVisitDto.getOwnerId());

            Map<String, Object> visitor = profileRepository.findProfileIdById(brandVisitDto.getVisitorId());

            if (owner.size() == 0) {
                logger.error("Failed to Fetch Brand Info, Invalid brand Id");
                return ResponseEntity.badRequest().body("Invalid profile ID");
            }else if(visitor.size() == 0){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
            else{
                rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, brandVisitDto.toJson());
                logger.info("\nProfile Info Fetched Successful: " + owner);
                return ResponseEntity.status(200).body(owner);
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    
}
