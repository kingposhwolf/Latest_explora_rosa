package com.example.demo.Services.ViewService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.SocialMedia.View.ViewDto;

public interface ViewService {
    ResponseEntity<Object> viewOperation(ViewDto viewDto);
}
