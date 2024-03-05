package com.example.demo.Dto;
/*
 * @author Dwight Danda
 *
 */
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UploadDataDto {

    @NotNull
    private  String path;
}
