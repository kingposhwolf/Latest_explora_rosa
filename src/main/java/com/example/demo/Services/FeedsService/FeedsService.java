package com.example.demo.Services.FeedsService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.SocialMedia.Post.PostRetrieveDto;

import jakarta.validation.constraints.NotNull;

public interface FeedsService {
    ResponseEntity<Object> retrieveFeeds(PostRetrieveDto postRetrieve);

    ResponseEntity<Object> retrieveUserOwnFeeds(@NotNull Long profileId);

    ResponseEntity<Object> retrieveUserFavoriteFeeds(@NotNull Long profileId);
}
