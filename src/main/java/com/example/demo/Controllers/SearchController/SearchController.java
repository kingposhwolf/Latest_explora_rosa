package com.example.demo.Controllers.SearchController;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
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

    // @PostMapping("/suggestiveFollowings")
    // public ResponseEntity<Object> searchFromFollowings(@RequestBody @Valid SearchDto searchDto, BindingResult bindingResult) {
    //     if (bindingResult.hasErrors()) {
    //         return globalValidationFormatter.validationFormatter(bindingResult);
    //     }
    //     return searchService.suggestiveProfilesOnFollowings(searchDto);
    // }
}
