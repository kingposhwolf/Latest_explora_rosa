package com.example.demo.Controllers.UserPost;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.InputDto.SocialMedia.Post.PostRetrieveDto;
import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.Services.FeedsService.FeedsServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/feeds")
public class PostRetrieveController {
    private final FeedsServiceImpl feedsServiceImpl;

    private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/fetch")
    public ResponseEntity<Object> retrievePosts(@RequestBody @Valid PostRetrieveDto postRetrieve, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return feedsServiceImpl.retrieveFeeds(postRetrieve);
    }

    @PostMapping("/fetch/specific")
    public ResponseEntity<Object> retrieveSpecificPosts(@RequestBody @Valid PostRetrieveDto postRetrieve, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return feedsServiceImpl.retrieveUserOwnFeeds(postRetrieve.getProfileId());
    }

    @PostMapping("/fetch/favorites")
    public ResponseEntity<Object> retrieveFavoritePosts(@RequestBody @Valid PostRetrieveDto postRetrieve, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return feedsServiceImpl.retrieveUserFavoriteFeeds(postRetrieve.getProfileId());
    }
}
