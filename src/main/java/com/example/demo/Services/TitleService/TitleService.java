package com.example.demo.Services.TitleService;

import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.TitleDto;

public interface TitleService {
    ResponseEntity<Object> getAllTitle();

    ResponseEntity<Object> saveTitle(TitleDto titleDto);
}
