package com.example.demo.Controllers.Favorites;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.DeleteFavoriteDto;
import com.example.demo.InputDto.FavoritesDto;
import com.example.demo.InputDto.FetchFavoritesDto;
import com.example.demo.Services.FavoritesService.FavoritesService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/favorites")
@AllArgsConstructor
public class FavoritesController {

    private final FavoritesService favoritesService;

    private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/add")
    public ResponseEntity<Object> newComment(@RequestBody @Valid FavoritesDto favoritesDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return favoritesService.addToFavorites(favoritesDto);
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteMessage(@RequestBody @Valid DeleteFavoriteDto deleteFavoriteDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return favoritesService.deleteFavorite(deleteFavoriteDto.getFavoriteId());
    }

    @PostMapping("/user")
    public ResponseEntity<Object> getCommentsForPost(@RequestBody @Valid FetchFavoritesDto fetchFavoritesDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return favoritesService.getUserFavorites(fetchFavoritesDto.getProfileId());
    }
}
