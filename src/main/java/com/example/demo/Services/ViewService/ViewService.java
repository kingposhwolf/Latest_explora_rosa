package com.example.demo.Services.ViewService;

import org.springframework.http.ResponseEntity;

import com.example.demo.Dto.ViewDto;

public interface ViewService {
    ResponseEntity<Object> viewOperation(ViewDto viewDto);
}
