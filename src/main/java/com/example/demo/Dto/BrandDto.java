package com.example.demo.Dto;
/*
 * @author Dwight Danda
 *
 */
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BrandDto {

    @NotNull
    @Size(max = 12)
    private String verificationStatus;

    @NotNull
    private Long userId;

    @NotNull
    private Long cityId;

    @NotNull
    private BigDecimal rates;

    @NotNull
    private String tinNumber;
}
