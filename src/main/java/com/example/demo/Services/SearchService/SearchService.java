package com.example.demo.Services.SearchService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.SearchDto.SearchDto;

public interface SearchService {
    ResponseEntity<Object> suggestiveProfiles(SearchDto searchDto);

    // ResponseEntity<Object> suggestiveProfilesOnFollowings(SearchDto searchDto);
}
