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
import java.util.List;

public interface UserPostService {

    ResponseEntity<Object> checkPostOwnership(Long postId, Long profileId);

    ResponseEntity<Object> checkPostContentType(UserPostDto userPostDto);

    @Transactional
    ResponseEntity<Object> uploadPost(
            UserPostDto userPostDto,
            MultipartFile file,
            Long profileId,
            String caption,
            Long brandId,
            List<String> hashtagNames) throws IOException;
}
