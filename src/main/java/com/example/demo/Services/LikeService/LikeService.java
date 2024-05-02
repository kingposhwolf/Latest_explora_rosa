package com.example.demo.Services.LikeService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.SocialMedia.Like.LikeDto;

import jakarta.validation.constraints.NotNull;

public interface LikeService {
    ResponseEntity<Object> likeOperation(LikeDto likeDto);

    ResponseEntity<Object> fetchLikes(@NotNull Long postId);
}
