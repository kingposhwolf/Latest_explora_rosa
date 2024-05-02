package com.example.demo.InputDto.Media;
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
