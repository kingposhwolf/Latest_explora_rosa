package com.example.demo.Dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.Models.HashTag;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserPostDto {

    private MultipartFile thumbnailFile;

    private final String folderPath="C:\\Users\\user\\Documents\\explore\\exploredev\\Posts\\";

    private Long id;

    @NotNull
    private Long profileId;

    private Long countryId;

    @NotNull
    private List<HashTag> hashTagIds;


    private Long brandId;


    private List<Long> likeIds;


    private String thumbnail;


    private String caption;

    @NotNull
    private LocalDateTime time;

    @NotNull
    private String type;

    @NotNull
    private String path = folderPath;


    private int shares;


    private int favorites;



}
