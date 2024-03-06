package com.example.demo.Services.LikeService;

import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.LikeDto;

public interface LikeService {
    ResponseEntity<Object> likeOperation(LikeDto likeDto);


    ResponseEntity<Object> deleteLike(Long likeId);
}
