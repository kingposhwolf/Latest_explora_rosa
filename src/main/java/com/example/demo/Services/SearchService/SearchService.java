package com.example.demo.Services.SearchService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.SearchDto.SearchDto;
import com.example.demo.InputDto.SearchDto.SearchHashTagDto;

public interface SearchService {
    ResponseEntity<Object> suggestiveProfiles(SearchDto searchDto);

    ResponseEntity<Object> fetchProfiles(SearchDto searchDto);

    ResponseEntity<Object> fetchHashTags(SearchHashTagDto hashTagDto);

    ResponseEntity<Object> fetchSearchHistory(Long profileId);

    ResponseEntity<Object> searchResults(SearchDto searchDto);

    ResponseEntity<Object> testPostResults(SearchDto searchDto);

    // ResponseEntity<Object> suggestiveProfilesOnFollowings(SearchDto searchDto);
}
