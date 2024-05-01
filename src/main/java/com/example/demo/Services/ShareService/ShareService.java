package com.example.demo.Services.ShareService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.SocialMedia.Share.ShareDto;


public interface ShareService {
    ResponseEntity<Object> shareOperation(ShareDto shareDto);
}
