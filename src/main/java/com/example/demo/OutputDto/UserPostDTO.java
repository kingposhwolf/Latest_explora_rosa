package com.example.demo.OutputDto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.Models.Country;
import com.example.demo.Models.HashTag;
import com.example.demo.Models.Tag;

import lombok.Data;

@Data
public class UserPostDTO {
    private Long id;
    private List<String> names;
    private Country country;
   private List<HashTag> hashTags;
   private List<Tag> tags;
    private int likes;
    private int shares;
    private int favorites;
    private Long brandId;
    private int comments;
    private String caption;
    private String thumbnail;
    private LocalDateTime time;
    private List<String> contentTypes;
    private String path;
}

