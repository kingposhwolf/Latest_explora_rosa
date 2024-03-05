package com.example.demo.Services.SuggestionService;

import com.example.demo.Dto.HashTagDto;
import org.springframework.http.ResponseEntity;

public interface HashTagService {
    ResponseEntity<Object> getAllHashTags();

    ResponseEntity<Object> saveHashTag(HashTagDto hashTagDto);
}
