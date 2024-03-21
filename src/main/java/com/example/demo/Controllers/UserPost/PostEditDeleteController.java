package com.example.demo.Controllers.UserPost;

import com.example.demo.InputDto.UserPostDto;
import com.example.demo.Services.UserPostService.UserPostServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/*
 * @author Dwight Danda
 *
 */
@RestController
@RequestMapping("/PostEditDelete")
public class PostEditDeleteController {
    private final UserPostServiceImpl userPostServiceImpl;

    public PostEditDeleteController(UserPostServiceImpl userPostServiceImpl) {
        this.userPostServiceImpl = userPostServiceImpl;
    }
    @PostMapping("/upload")
    public ResponseEntity<Object> uploadPost(
            @ModelAttribute UserPostDto userPostDto,
            @RequestParam("content") MultipartFile[] file,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam("profileId") Long profileId,
            @RequestParam("caption") String caption,
            @RequestParam(value = "brandName", required = false) Long brandId,
            @RequestParam(value = "hashTags", required = false) List<String> hashTags) {
        try {
            return userPostServiceImpl.uploadPost(userPostDto, file, profileId, caption, brandId, hashTags);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload post: " + e.getMessage());
        }
    }

    @PostMapping("/checkOwnership")
    public ResponseEntity<Object> checkPostOwnership(@RequestParam Long postId, @RequestParam Long profileId) {
        try {
            ResponseEntity<Object> ownershipResponse = userPostServiceImpl.checkPostOwnership(postId, profileId);
            return ownershipResponse;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to check post ownership: " + e.getMessage());
        }
    }

//    @PostMapping("/checkContentType")
//    public ResponseEntity<Object> checkPostContentType(@RequestBody UserPostDto userPostDto) {
//        try {
//            ResponseEntity<Object> contentTypeResponse = userPostServiceImpl.checkPostContentType(userPostDto);
//            return contentTypeResponse;
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to check post content type: " + e.getMessage());
//        }
//    }
    @PostMapping("/{postId}/editCaption")
    public ResponseEntity<Object> editCaption(
            @PathVariable Long postId,
            @RequestParam Long profileId,
            @RequestBody UserPostDto userPostDto) {

        userPostDto.setId(postId); // Set the postId in the DTO

        try {
            ResponseEntity<Object> editCaptionResponse = userPostServiceImpl.editCaption(userPostDto, profileId);
            return editCaptionResponse;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to edit post caption: " + e.getMessage());
        }
    }
}
