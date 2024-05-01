package com.example.demo.InputDto.SocialMedia.Post;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Models.SocialMedia.HashTag;

@Data
public class UserPostDto {

    private MultipartFile thumbnailFile;

    private final String folderPath="C:\\Users\\user\\Documents\\explore\\exploredev\\Posts\\";

    private Long id;

    private List<String> names;

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
    private List<String> contentTypes;

    @NotNull
    private String path = folderPath;


    private int shares;


    private int favorites;



}
