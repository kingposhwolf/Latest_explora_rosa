package com.example.demo.InputDto.Ecommerce.Cart;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartDto {
    @NotNull
    private Long profileId;

    @NotNull
    private List<CartItemDto> items;

}
