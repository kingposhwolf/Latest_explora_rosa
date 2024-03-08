package com.example.demo.Services.UserPostService;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Dto.UserPostDto;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserPostService {
    ResponseEntity<Object> uploadPost(UserPostDto userPostDto, MultipartFile file, Long profileId) throws IOException;

    ResponseEntity<Object> checkPostOwnership(Long postId, Long profileId);

    ResponseEntity<Object> checkPostContentType(UserPostDto userPostDto);
}
