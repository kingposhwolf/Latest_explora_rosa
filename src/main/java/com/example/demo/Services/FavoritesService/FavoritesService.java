package com.example.demo.Services.FavoritesService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.SocialMedia.Favorites.DeleteFavoriteDto;
import com.example.demo.InputDto.SocialMedia.Favorites.FavoritesDto;


public interface FavoritesService {
    ResponseEntity<Object> addToFavorites(FavoritesDto favoritesDto);

    ResponseEntity<Object> deleteFavorite(DeleteFavoriteDto deleteFavoriteDto);
}
