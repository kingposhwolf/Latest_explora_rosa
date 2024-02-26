package com.example.demo.Dto;
/*
 * @author Dwight Danda
 *
 */
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UploadDto {

    @NotNull
    private  String path;
}
