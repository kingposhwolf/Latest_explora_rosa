package com.example.demo.Services.UserPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.InputDto.SocialMedia.Post.UserPostDto;

import java.io.IOException;
import java.util.List;

public interface UserPostService {

    @Transactional
    ResponseEntity<Object> uploadPost(
            UserPostDto userPostDto,
            MultipartFile[] files,
            Long profileId,
            String caption,
            String location,
            Long brandId,
            List<String> hashtagNames,
            Long countryId) throws IOException;

    @Transactional
    ResponseEntity<Object> uploadSnippet(
            UserPostDto userPostDto,
            MultipartFile videoSnippet,
            MultipartFile thumbNail,
            Long profileId,
            String caption,
            Long brandId,
            List<String> hashtagNames) throws IOException;


    ResponseEntity<Object> checkPostOwnership(Long postId, Long profileId);

    @Transactional
    ResponseEntity<Object> editCaption(UserPostDto userPostDto, Long profileId);

}

//    ResponseEntity<Object> checkPostContentType(UserPostDto userPostDto);

//    @Transactional
//    ResponseEntity<Object> uploadPost(
//            UserPostDto userPostDto,
//            MultipartFile file,
//            Long profileId,
//            String caption,
//            Long brandId,
//            List<String> hashtagNames) throws IOException;
//ResponseEntity<Object> viewPost(Long postId) throws IOException;
