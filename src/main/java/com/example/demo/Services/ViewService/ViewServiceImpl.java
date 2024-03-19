package com.example.demo.Services.ViewService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.ViewDto;
import com.example.demo.Services.ShareService.ShareServiceImpl;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ViewServiceImpl implements ViewService{
    private static final Logger logger = LoggerFactory.getLogger(ShareServiceImpl.class);

     private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "View";
    private static final String ROUTING_KEY = "viewOperation";

    @Override
    public ResponseEntity<Object> viewOperation(ViewDto viewDto) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, viewDto.toJson());
            
            logger.info("Added to Queue for tracking Views successfully: ");
            return ResponseEntity.ok("View operation complete successfully!");
        } catch (Exception e) {
            logger.error("Failed to add to complete view operation, Server Error : ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
}