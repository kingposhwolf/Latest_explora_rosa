package com.example.demo.Controllers.UserPost;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.Services.FeedsService.FeedsServiceImpl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/feeds")
public class PostRetrieveController {
    private final FeedsServiceImpl feedsServiceImpl;

    private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/save")
    public ResponseEntity<Object> retrievePosts(@RequestBody @Valid @NotNull Long profileId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return feedsServiceImpl.retrieveFeeds(profileId);
     }
}
