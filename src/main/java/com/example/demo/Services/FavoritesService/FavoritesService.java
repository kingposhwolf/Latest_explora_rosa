package com.example.demo.Services.FavoritesService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.DeleteFavoriteDto;
import com.example.demo.InputDto.FavoritesDto;

import jakarta.validation.constraints.NotNull;

public interface FavoritesService {
    ResponseEntity<Object> addToFavorites(FavoritesDto favoritesDto);

    ResponseEntity<Object> getUserFavorites(@NotNull Long profileId);

    ResponseEntity<Object> deleteFavorite(DeleteFavoriteDto deleteFavoriteDto);
}
