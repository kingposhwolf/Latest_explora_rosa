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

    @PostMapping("/results")
    public ResponseEntity<Object> searchResults(@RequestBody @Valid SearchDto searchDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return searchService.searchResults(searchDto);
    }

    // @PostMapping("/suggestiveFollowings")
    // public ResponseEntity<Object> searchFromFollowings(@RequestBody @Valid SearchDto searchDto, BindingResult bindingResult) {
    //     if (bindingResult.hasErrors()) {
    //         return globalValidationFormatter.validationFormatter(bindingResult);
    //     }
    //     return searchService.suggestiveProfilesOnFollowings(searchDto);
    // }

    @PostMapping("/test")
    public ResponseEntity<Object> testResults(@RequestBody @Valid SearchDto searchDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return searchService.testPostResults(searchDto);
    }

    @PostMapping("/profilesResults")
    public ResponseEntity<Object> fetchProfiles(@RequestBody @Valid SearchDto searchDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return searchService.fetchProfiles(searchDto);
    }
}
