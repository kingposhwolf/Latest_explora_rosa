package com.example.demo.Components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.demo.Dto.LikeDto;

@Component
public class RabbitMqConsumers {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConsumers.class);

    @RabbitListener(queues = "saveLike")
    public void likeSave(String message) throws Exception {
        LikeDto likeDto = LikeDto.fromJson(message);
    }

    @RabbitListener(queues = "trackLike")
    public void likeTracking(String message) throws Exception {
        LikeDto likeDto = LikeDto.fromJson(message);
    }
}