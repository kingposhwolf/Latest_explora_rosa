package com.example.demo.OutputDto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.Models.Information.Country;
import com.example.demo.Models.SocialMedia.HashTag;
import com.example.demo.Models.SocialMedia.Mention;
import com.example.demo.Models.SocialMedia.Interactions.Tag;

import lombok.Data;

@Data
public class UserPostDTO {
    private Long id;
    private List<String> names;
    private Country country;
    private List<HashTag> hashTags;
    private List<Tag> tags;
    private List<Mention> mentions;
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


    public UserPostDTO(Long id,
                       List<String> names,
                       Country country,
                       List<HashTag> hashTags,
                       List<Tag> tags,
                       List<Mention> mentions,
                       int likes,
                       int shares,
                       int favorites,
                       int comments,
                       String caption,
                       String thumbnail,
                       LocalDateTime time,
                       List<String> contentTypes,
                       String path) {
        this.id = id;
        this.names = names;
        this.country = country;
        this.hashTags = hashTags;
        this.tags = tags;
        this.mentions = mentions;
        this.likes = likes;
        this.shares = shares;
        this.favorites = favorites;
        this.comments = comments;
        this.caption = caption;
        this.thumbnail = thumbnail;
        this.time = time;
        this.contentTypes = contentTypes;
        this.path = path;
    }
}

