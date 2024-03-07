package com.example.demo.Controllers.UserPost;

import com.example.demo.Dto.UserPostDto;
import com.example.demo.Services.UserPostService.UserPostServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "thumbnailFile", required = false) MultipartFile thumbnailFile,
            @RequestParam("profileId") Long profileId
    ) {
        try {
            return userPostServiceImpl.uploadPost(userPostDto, file, profileId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload post: " + e.getMessage());
        }
    }
}
