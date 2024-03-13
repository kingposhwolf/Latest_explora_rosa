package com.example.demo.Services.FeedsService;

import org.springframework.http.ResponseEntity;

import jakarta.validation.constraints.NotNull;

public interface FeedsService {
    ResponseEntity<Object> retrieveFeeds(@NotNull Long profileId);
}
