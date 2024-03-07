package com.example.demo.Services.UserPostService;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Dto.UserPostDto;
import com.example.demo.Models.*;
import com.example.demo.Repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserPostServiceImpl implements UserPostService{
    private static final Logger logger = LoggerFactory.getLogger(UserPostServiceImpl.class);

    private final String folderPath="C:\\Users\\user\\Documents\\explore\\exploredev\\Posts\\";

    private final UserPostRepository userPostRepository;
    private final ProfileRepository profileRepository;
    private final CountryRepository countryRepository;
    private final HashTagRepository hashTagRepository;
    private final BrandRepository brandRepository;
    private final LikeRepository likeRepository;

    public UserPostServiceImpl(UserPostRepository userPostRepository,
                               ProfileRepository profileRepository,
                               CountryRepository countryRepository,
                               HashTagRepository hashTagRepository,
                               BrandRepository brandRepository,
                               LikeRepository likeRepository) {
        this.userPostRepository = userPostRepository;
        this.profileRepository = profileRepository;
        this.countryRepository = countryRepository;
        this.hashTagRepository = hashTagRepository;
        this.brandRepository = brandRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<Object> uploadPost(UserPostDto userPostDto, MultipartFile file, Long profileId) throws IOException {
        try {
            // Check if the content type is allowed
            String contentType = file.getContentType();
            if (!isValidContentType(contentType)) {
                logger.error("Unsupported content type: {}", contentType);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported content type: " + contentType);
            }

            // Fetch profile and country entities
            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new IllegalArgumentException("Profile with ID " + profileId + " not found"));
            Country country = countryRepository.findById(userPostDto.getCountryId())
                    .orElseThrow(() -> new IllegalArgumentException("Country with ID " + userPostDto.getCountryId() + " not found"));

            // Fetch hash tag entities
            List<HashTag> hashTags = new ArrayList<>();
            for (Long hashTagId : userPostDto.getHashTagIds()) {
                HashTag hashTag = hashTagRepository.findById(hashTagId)
                        .orElseThrow(() -> new IllegalArgumentException("HashTag with ID " + hashTagId + " not found"));
                hashTags.add(hashTag);
            }

            // Fetch brand entity
            Brand brand = null;
            if (userPostDto.getBrandId() != null) {
                brand = brandRepository.findById(userPostDto.getBrandId())
                        .orElseThrow(() -> new IllegalArgumentException("Brand with ID " + userPostDto.getBrandId() + " not found"));
            }

            // Validate thumbnail if the post is a video
            String thumbnail = null;
            if (userPostDto.getType().equalsIgnoreCase("video")) {
                // Check if the user provided a thumbnail file
                MultipartFile thumbnailFile = userPostDto.getThumbnailFile();
                if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
                    thumbnail = processThumbnail(file, thumbnailFile);
                } else {
                    // Use FFmpeg to extract a frame from the video and save it as a thumbnail
                    thumbnail = processThumbnail(file, null);
                }
            }

            // Save the user post
            UserPost userPost = new UserPost();
            userPost.setProfile(profile);
            userPost.setCountry(country);
            userPost.setHashTags(hashTags);
            userPost.setBrand(brand);
            userPost.setThumbnail(thumbnail);
            userPost.setCaption(userPostDto.getCaption());
            userPost.setTime(LocalDateTime.now());
            userPost.setType(userPostDto.getType());
            userPost.setPath(userPostDto.getPath());
            userPost.setShares(0);
            userPost.setFavorites(0);

            // Save the post to the database
            UserPost savedPost = userPostRepository.save(userPost);

            // Save the file to the file system
            String uploadPath = "C:\\Users\\user\\Documents\\explore\\exploredev\\Posts\\";
            file.transferTo(new File(uploadPath));

            logger.info("Post uploaded successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body("Post uploaded successfully");
        } catch (Exception e) {
            logger.error("Failed to upload post: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload post: " + e.getMessage());
        }
    }

//    @Override
//    @Transactional
//    public ResponseEntity<Object> uploadPost(UserPostDto userPostDto, MultipartFile file) throws IOException {
//        try {
//            // Check if the content type is allowed
//            String contentType = file.getContentType();
//            if (!isValidContentType(contentType)) {
//                logger.error("Unsupported content type: {}", contentType);
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported content type: " + contentType);
//            }
//
//            // Fetch profile and country entities
//            Profile profile = profileRepository.findById(userPostDto.getProfileId())
//                    .orElseThrow(() -> new IllegalArgumentException("Profile with ID " + userPostDto.getProfileId() + " not found"));
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
//            // Save the file to the file system
//            String uploadPath = "C:\\Users\\user\\Documents\\explore\\exploredev\\Posts\\";
//            file.transferTo(new File(uploadPath));
//
//            logger.info("Post uploaded successfully");
//            return ResponseEntity.status(HttpStatus.CREATED).body("Post uploaded successfully");
//        } catch (Exception e) {
//            logger.error("Failed to upload post: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload post: " + e.getMessage());
//        }
//    }
    private boolean isValidContentType(String contentType) {
        // Check if the content type is not null and matches the allowed types
        return contentType != null && (contentType.startsWith("image/") || contentType.startsWith("video/"));
    }
    private String processThumbnail(MultipartFile file, MultipartFile thumbnailFile) throws IOException {
        // Generate a unique filename for the thumbnail
        String thumbnailFileName = "thumbnail_" + System.currentTimeMillis() + ".jpg";

        // Define the directory where thumbnails will be stored (same as posts directory)
        String thumbnailDirectory = folderPath;

        // Ensure the directory exists, create it if necessary
        File directory = new File(thumbnailDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate the full path for the thumbnail file
        String thumbnailFilePath = thumbnailDirectory + File.separator + thumbnailFileName;

        // Check if the user provided a thumbnail image
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            try {
                // Save the selected thumbnail image
                Files.copy(thumbnailFile.getInputStream(), Paths.get(thumbnailFilePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Error saving thumbnail: " + e.getMessage());
            }
        } else {
            try {
                // Use FFmpeg to extract a frame from the video and save it as a thumbnail
                ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", file.getOriginalFilename(), "-vframes", "1", "-an", "-s", "128x128", "-ss", "00:00:01", thumbnailFilePath);
                Process process = processBuilder.start();

                // Wait for the process to complete
                process.waitFor();

                // Check if the thumbnail file was created successfully
                File thumbnailFileFromVideo = new File(thumbnailFilePath);
                if (!thumbnailFileFromVideo.exists()) {
                    throw new IOException("Failed to generate thumbnail");
                }
            } catch (IOException | InterruptedException e) {
                throw new IOException("Error generating thumbnail: " + e.getMessage());
            }
        }

        // Return the path to the selected or generated thumbnail
        return thumbnailFilePath;
    }


}
