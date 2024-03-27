package com.example.demo.Services.BrandService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.InputDto.ProfileVisitDto;
import com.example.demo.Models.UserManagement.BussinessAccount.Brand;
import com.example.demo.Repositories.BrandRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService{

    private static final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    private final BrandRepository brandRepository;

    private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "ProfileVisit";
    private static final String ROUTING_KEY = "profileVisitOperation";

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> getBrandById(ProfileVisitDto brandVisitDto) {

        try {
            Optional<Brand> owner = brandRepository.findById(brandVisitDto.getOwnerId());

            Optional<Brand> visitor = brandRepository.findById(brandVisitDto.getVisitorId());

            if (!owner.isPresent()) {
                logger.error("Failed to Fetch Brand Info, Invalid brand Id");
                return ResponseEntity.badRequest().body("Invalid profile ID");
            }else if(!visitor.isPresent()){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
             else{
                rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, brandVisitDto.toJson());
                logger.info("\nProfile Info Fetched Successful: " + owner);
                return ResponseEntity.status(200).body(owner.get());
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    
}
