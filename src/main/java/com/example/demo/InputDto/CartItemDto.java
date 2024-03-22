package com.example.demo.InputDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemDto {

    @NotNull
    private Long postId;

    @NotNull
    private int quantity;

}
