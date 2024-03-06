package com.example.demo.Services.LikeService;

import org.springframework.http.ResponseEntity;

public interface LikeService {
    ResponseEntity<Object> saveLike(Long profileId);


    ResponseEntity<Object> deleteLike(Long commentId);
}
