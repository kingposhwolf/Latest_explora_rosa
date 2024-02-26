package com.example.demo.Dto;
/*
 * @author Dwight Danda
 *
 */
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerificationUploadDto {

    @NotNull
    private Long brandId;

    @NotNull
    private Long uploadId;
}
