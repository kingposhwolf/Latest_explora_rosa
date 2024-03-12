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

//    ResponseEntity<Object> uploadPost(UserPostDto userPostDto, MultipartFile file, Long profileId) throws IOException;

    //    @Override
    //    @Transactional
    //    public ResponseEntity<Object> uploadPost(UserPostDto userPostDto, MultipartFile file, Long profileId) throws IOException {
    //        try {
    //            // Check if the content type is allowed
    //            String contentType = file.getContentType();
    //            if (!isValidContentType(contentType)) {
    //                logger.error("Unsupported content type: {}", contentType);
    //                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported content type: " + contentType);
    //            }
    //
    //            // Fetch profile and country entities
    //            Profile profile = profileRepository.findById(profileId)
    //                    .orElseThrow(() -> new IllegalArgumentException("Profile with ID " + profileId + " not found"));
    //            Country country = countryRepository.findById(userPostDto.getCountryId())
    //                    .orElseThrow(() -> new IllegalArgumentException("Country with ID " + userPostDto.getCountryId() + " not found"));
    //
    //            // Fetch hash tag entities
    //            List<HashTag> hashTags = new ArrayList<>();
    //            for (Long hashTagId : userPostDto.getHashTagIds()) {
    //                HashTag hashTag = hashTagRepository.findById(hashTagId)
    //                        .orElseThrow(() -> new IllegalArgumentException("HashTag with ID " + hashTagId + " not found"));
    //                hashTags.add(hashTag);
    //            }
    //
    //            // Fetch brand entity
    //            Brand brand = null;
    //            if (userPostDto.getBrandId() != null) {
    //                brand = brandRepository.findById(userPostDto.getBrandId())
    //                        .orElseThrow(() -> new IllegalArgumentException("Brand with ID " + userPostDto.getBrandId() + " not found"));
    //            }
    //
    //            // Validate thumbnail if the post is a video
    //            String thumbnail = null;
    //            if (userPostDto.getType().equalsIgnoreCase("video")) {
    //                // Check if the user provided a thumbnail file
    //                MultipartFile thumbnailFile = userPostDto.getThumbnailFile();
    //                if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
    //                    thumbnail = processThumbnail(file, thumbnailFile);
    //                } else {
    //                    // Use FFmpeg to extract a frame from the video and save it as a thumbnail
    //                    thumbnail = processThumbnail(file, null);
    //                }
    //            }
    //            // Save the file to the file system
    //            String uploadPath = "C:\\Users\\user\\Documents\\explore\\exploredev\\Posts\\";
    //            userPostDto.setPath(uploadPath);
    //
    //            // Save the user post
    //            UserPost userPost = new UserPost();
    //            userPost.setProfile(profile);
    //            userPost.setCountry(country);
    //            userPost.setHashTags(hashTags);
    //            userPost.setBrand(brand);
    //            userPost.setThumbnail(thumbnail);
    //            userPost.setCaption(userPostDto.getCaption());
    //            userPost.setTime(LocalDateTime.now());
    //            userPost.setType(userPostDto.getType());
    //            userPost.setPath(userPostDto.getPath());
    //            userPost.setShares(0);
    //            userPost.setFavorites(0);
    //
    //            // Save the post to the database
    //            UserPost savedPost = userPostRepository.save(userPost);
    //
    //            file.transferTo(new File(uploadPath));
    //
    //            logger.info("Post uploaded successfully");
    //            return ResponseEntity.status(HttpStatus.CREATED).body("Post uploaded successfully");
    //        } catch (Exception e) {
    //            logger.error("Failed to upload post: {}", e.getMessage());
    //            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload post: " + e.getMessage());
    //        }
    //    }
//    @Transactional
//    ResponseEntity<Object> uploadPost(
//            UserPostDto userPostDto,
//            MultipartFile file,
//            Long profileId,
//            String caption,
//            String brandName,
//            List<String> hashtagNames) throws IOException;

    ResponseEntity<Object> checkPostOwnership(Long postId, Long profileId);

    ResponseEntity<Object> checkPostContentType(UserPostDto userPostDto);

    //    @Override
    //    @Transactional
    //    public ResponseEntity<Object> uploadPost(UserPostDto userPostDto, MultipartFile file, Long profileId) throws IOException {
    //        try {
    //            // Check if the content type is allowed
    //            String contentType = file.getContentType();
    //            if (!isValidContentType(contentType)) {
    //                logger.error("Unsupported content type: {}", contentType);
    //                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported content type: " + contentType);
    //            }
    //
    //            // Fetch profile and country entities
    //            Profile profile = profileRepository.findById(profileId)
    //                    .orElseThrow(() -> new IllegalArgumentException("Profile with ID " + profileId + " not found"));
    //            Country country = countryRepository.findById(userPostDto.getCountryId())
    //                    .orElseThrow(() -> new IllegalArgumentException("Country with ID " + userPostDto.getCountryId() + " not found"));
    //
    //            // Fetch hash tag entities
    //            List<HashTag> hashTags = new ArrayList<>();
    //            for (Long hashTagId : userPostDto.getHashTagIds()) {
    //                HashTag hashTag = hashTagRepository.findById(hashTagId)
    //                        .orElseThrow(() -> new IllegalArgumentException("HashTag with ID " + hashTagId + " not found"));
    //                hashTags.add(hashTag);
    //            }
    //
    //            // Fetch brand entity
    //            Brand brand = null;
    //            if (userPostDto.getBrandId() != null) {
    //                brand = brandRepository.findById(userPostDto.getBrandId())
    //                        .orElseThrow(() -> new IllegalArgumentException("Brand with ID " + userPostDto.getBrandId() + " not found"));
    //            }
    //
    //            // Validate thumbnail if the post is a video
    //            String thumbnail = null;
    //            if (userPostDto.getType().equalsIgnoreCase("video")) {
    //                // Check if the user provided a thumbnail file
    //                MultipartFile thumbnailFile = userPostDto.getThumbnailFile();
    //                if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
    //                    thumbnail = processThumbnail(file, thumbnailFile);
    //                } else {
    //                    // Use FFmpeg to extract a frame from the video and save it as a thumbnail
    //                    thumbnail = processThumbnail(file, null);
    //                }
    //            }
    //            // Save the file to the file system
    //            String uploadPath = "C:\\Users\\user\\Documents\\explore\\exploredev\\Posts\\";
    //            userPostDto.setPath(uploadPath);
    //
    //            // Save the user post
    //            UserPost userPost = new UserPost();
    //            userPost.setProfile(profile);
    //            userPost.setCountry(country);
    //            userPost.setHashTags(hashTags);
    //            userPost.setBrand(brand);
    //            userPost.setThumbnail(thumbnail);
    //            userPost.setCaption(userPostDto.getCaption());
    //            userPost.setTime(LocalDateTime.now());
    //            userPost.setType(userPostDto.getType());
    //            userPost.setPath(userPostDto.getPath());
    //            userPost.setShares(0);
    //            userPost.setFavorites(0);
    //
    //            // Save the post to the database
    //            UserPost savedPost = userPostRepository.save(userPost);
    //
    //            file.transferTo(new File(uploadPath));
    //
    //            logger.info("Post uploaded successfully");
    //            return ResponseEntity.status(HttpStatus.CREATED).body("Post uploaded successfully");
    //        } catch (Exception e) {
    //            logger.error("Failed to upload post: {}", e.getMessage());
    //            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload post: " + e.getMessage());
    //        }
    //    }
    @Transactional
    ResponseEntity<Object> uploadPost(
            UserPostDto userPostDto,
            MultipartFile file,
            Long profileId,
            String caption,
            Long brandId,
            List<String> hashtagNames) throws IOException;
}
