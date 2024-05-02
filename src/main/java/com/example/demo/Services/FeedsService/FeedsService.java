package com.example.demo.Services.FeedsService;

import org.springframework.http.ResponseEntity;

import jakarta.validation.constraints.NotNull;

public interface FeedsService {
    ResponseEntity<Object> retrieveFeeds(@NotNull Long profileId);

    ResponseEntity<Object> retrieveUserOwnFeeds(@NotNull Long profileId);

    ResponseEntity<Object> retrieveUserFavoriteFeeds(@NotNull Long profileId);
}
