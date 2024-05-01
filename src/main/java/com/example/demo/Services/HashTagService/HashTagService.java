package com.example.demo.Services.HashTagService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.SocialMedia.HashTag.HashTagDto;

public interface HashTagService {
    ResponseEntity<Object> getAllHashTags();

    ResponseEntity<Object> saveHashTag(HashTagDto hashTagDto);

    ResponseEntity<Object> getHashTagByName(String name);
}
