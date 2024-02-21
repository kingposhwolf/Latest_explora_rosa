/*
 * @author Dwight Danda
 *
 */
package com.example.demo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountTypeDto {
    @NotBlank
    @Size(max = 100)
    private String name;
}
