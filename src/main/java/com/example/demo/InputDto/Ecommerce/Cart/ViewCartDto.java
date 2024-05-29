package com.example.demo.InputDto.Ecommerce.Cart;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ViewCartDto {
    @NotNull
    private Long profileId;
}
