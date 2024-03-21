package com.example.demo.Services.TitleService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.TitleDto;

public interface TitleService {
    ResponseEntity<Object> getAllTitle();

    ResponseEntity<Object> saveTitle(TitleDto titleDto);
}
