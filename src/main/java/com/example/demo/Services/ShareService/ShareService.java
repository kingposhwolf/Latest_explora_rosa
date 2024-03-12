package com.example.demo.Services.ShareService;

import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.ShareDto;


public interface ShareService {
    ResponseEntity<Object> shareOperation(ShareDto shareDto);
}
