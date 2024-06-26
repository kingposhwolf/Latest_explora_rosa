package com.example.demo.Controllers.SearchController;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.SearchDto.FetchSearchHistoryDto;
import com.example.demo.InputDto.SearchDto.SearchDto;
import com.example.demo.InputDto.SearchDto.SearchHashTagDto;
import com.example.demo.Services.SearchService.SearchServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {
    private final SearchServiceImpl searchService;

    private GlobalValidationFormatter globalValidationFormatter;

    @PostMapping("/suggestive")
    public ResponseEntity<Object> searchFromEngage(@RequestBody @Valid SearchDto searchDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return searchService.suggestiveProfiles(searchDto);
    }

    @PostMapping("/history")
    public ResponseEntity<Object> searchHistory(@RequestBody @Valid FetchSearchHistoryDto historyDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return searchService.fetchSearchHistory(historyDto.getProfileId());
    }

    @PostMapping("/results/profiles")
    public ResponseEntity<Object> searchResults(@RequestBody @Valid SearchDto searchDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return searchService.profileResults(searchDto);
    }

    // @PostMapping("/suggestiveFollowings")
    // public ResponseEntity<Object> searchFromFollowings(@RequestBody @Valid SearchDto searchDto, BindingResult bindingResult) {
    //     if (bindingResult.hasErrors()) {
    //         return globalValidationFormatter.validationFormatter(bindingResult);
    //     }
    //     return searchService.suggestiveProfilesOnFollowings(searchDto);
    // }

    @PostMapping("/results/posts")
    public ResponseEntity<Object> testResults(@RequestBody @Valid SearchDto searchDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return searchService.searchPostResults(searchDto);
    }

    @PostMapping("/hashTagResults")
    public ResponseEntity<Object> fetchHashTag(@RequestBody @Valid SearchHashTagDto searchDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return searchService.fetchHashTags(searchDto);
    }

    @PostMapping("/discover")
    public ResponseEntity<Object> searchDiscover(@RequestBody @Valid FetchSearchHistoryDto discoverDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return searchService.discover(discoverDto.getProfileId());
    }
}
