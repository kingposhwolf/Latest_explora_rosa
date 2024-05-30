package com.example.demo.Services.CartService;

import org.springframework.http.ResponseEntity;

import com.example.demo.InputDto.Ecommerce.Cart.CartDto;
import com.example.demo.InputDto.Ecommerce.Cart.ViewCartDto;

public interface CartService {
    ResponseEntity<Object> addToCart(CartDto cartDto);

    ResponseEntity<Object> removeToCart(CartDto cartDto);

    ResponseEntity<Object> viewCart(ViewCartDto viewCartDto);
}
