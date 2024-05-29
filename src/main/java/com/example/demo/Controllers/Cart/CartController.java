package com.example.demo.Controllers.Cart;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Services.CartService.CartServiceImpl;

import jakarta.validation.Valid;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.Ecommerce.Cart.CartDto;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping ("/cart")
@AllArgsConstructor
public class CartController {
    private GlobalValidationFormatter globalValidationFormatter;

    private final CartServiceImpl CartService;

    @PostMapping("/add-item")
    public ResponseEntity<Object> addItem(@RequestBody @Valid CartDto cartDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return CartService.addToCart(cartDto);
    }

    @PostMapping("/remove-item")
    public ResponseEntity<Object> removeItem(@RequestBody @Valid CartDto cartDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return CartService.removeToCart(cartDto);
    }
}
