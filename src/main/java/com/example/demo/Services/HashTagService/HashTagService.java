package com.example.demo.Services.HashTagService;

import com.example.demo.Dto.HashTagDto;
import org.springframework.http.ResponseEntity;

public interface HashTagService {
    ResponseEntity<Object> getAllHashTags();

    ResponseEntity<Object> saveHashTag(HashTagDto hashTagDto);

    ResponseEntity<Object> getHashTagByName(String name);
}
