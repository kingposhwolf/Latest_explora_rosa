package com.example.demo.Services.CartService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.CartDto;

public interface CartService {
    ResponseEntity<Object> addToCart(CartDto cartDto);

    ResponseEntity<Object> removeToCart(CartDto cartDto);
}
