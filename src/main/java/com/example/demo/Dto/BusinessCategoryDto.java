package com.example.demo.Dto;
import java.util.List;

/*
 * @author Dwight Danda
 *
 */
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BusinessCategoryDto {
    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    List<Long> hashTags;
}
