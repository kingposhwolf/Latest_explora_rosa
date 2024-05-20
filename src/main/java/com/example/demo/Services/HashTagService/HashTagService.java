package com.example.demo.Services.HashTagService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.SocialMedia.HashTag.HashTagDto;
import com.example.demo.InputDto.SocialMedia.HashTag.PostsHashTag;

public interface HashTagService {
    ResponseEntity<Object> getAllHashTags();

    ResponseEntity<Object> saveHashTag(HashTagDto hashTagDto);

    ResponseEntity<Object> getHashTagByName(String name);

    ResponseEntity<Object> getPostsHashTags(PostsHashTag postsHashTag);
}
