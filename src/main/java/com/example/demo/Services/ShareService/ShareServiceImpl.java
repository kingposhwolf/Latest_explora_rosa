package com.example.demo.Services.ShareService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.ShareDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ShareServiceImpl implements ShareService{

    private static final Logger logger = LoggerFactory.getLogger(ShareServiceImpl.class);

     private AmqpTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "Share";
    private static final String ROUTING_KEY = "shareOperation";

    @Override
    public ResponseEntity<Object> shareOperation(ShareDto shareDto) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, shareDto.toJson());
            
            logger.info("Added to Queue for tracking Share successfully: ");
            return ResponseEntity.ok("Share operation complete successfully!");
        } catch (Exception e) {
            logger.error("Failed to add to complete share operation, Server Error : ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
}
