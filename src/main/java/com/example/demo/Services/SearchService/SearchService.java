package com.example.demo.Services.SearchService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.SearchDto.SearchDto;
import com.example.demo.InputDto.SearchDto.SearchHashTagDto;

public interface SearchService {
    ResponseEntity<Object> suggestiveProfiles(SearchDto searchDto);

    ResponseEntity<Object> fetchHashTags(SearchHashTagDto hashTagDto);

    ResponseEntity<Object> fetchSearchHistory(Long profileId);

    ResponseEntity<Object> discover(Long profileId);

    ResponseEntity<Object> profileResults(SearchDto searchDto);

    ResponseEntity<Object> searchPostResults(SearchDto searchDto);

    // ResponseEntity<Object> suggestiveProfilesOnFollowings(SearchDto searchDto);
}
