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
import java.util.Optional;

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
@Override
@Transactional
public ResponseEntity<Object> uploadPost(
        UserPostDto userPostDto,
        MultipartFile file,
        Long profileId,
        String caption,
        Long brandId,
        List<String> hashtagNames) throws IOException {
    try {
        // Check if the content type is allowed
        String contentType = file.getContentType();
        if (!isValidContentType(contentType)) {
            logger.error("Unsupported content type: {}", contentType);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported content type: " + contentType);
        }
        userPostDto.setType(contentType);

        // Fetch profile and country entities
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Profile with ID " + profileId + " not found"));
        Country country = countryRepository.findById(userPostDto.getCountryId())
                .orElseThrow(() -> new IllegalArgumentException("Country with ID " + userPostDto.getCountryId() + " not found"));
        userPostDto.setProfileId(profileId);

        //Fetch or create new hashTag
        List<HashTag> hashTags = new ArrayList<>();
        for (String hashtagName : hashtagNames) {
            if (hashtagName != null) {
                Optional<HashTag> optionalHashTag = hashTagRepository.findByName(hashtagName);
                HashTag hashTag = null;
                if (optionalHashTag.isPresent()) {
                    hashTag = optionalHashTag.get();
                } else {
                    // Create a new hashtag if it doesn't exist
                    hashTag = new HashTag();
                    hashTag.setName(hashtagName);
                    hashTagRepository.save(hashTag); // Save the new hash tag to get the ID
                }
                hashTags.add(hashTag);
            }
        }
        // Set the list of hashtags to the userPostDto
        userPostDto.setHashTagIds(hashTags);


            // Fetch brand entity
            Brand brand = null;
            userPostDto.setBrandId(brandId);
            if (userPostDto.getBrandId() != null) {
                brand = brandRepository.findById(brandId)
                        .orElseThrow(() -> new IllegalArgumentException("Brand with ID " + userPostDto.getBrandId() + " not found"));
            }


            // Validate thumbnail if the post is a video
            if (userPostDto.getType().equalsIgnoreCase("video")) {
                // Check if the user provided a thumbnail file
                MultipartFile thumbnailFile = userPostDto.getThumbnailFile();
                if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
                    String thumbnail = null;
                    thumbnail = processThumbnail(file, thumbnailFile);
                    userPostDto.setThumbnail(thumbnail);
                } else {
                    String thumbnail = null;
                    // Use FFmpeg to extract a frame from the video and save it as a thumbnail
                    thumbnail = processThumbnail(file, null);
                    userPostDto.setThumbnail(thumbnail);
                }
            }



            // Save the user post
            UserPost userPost = new UserPost();
            userPost.setProfile(profile);
            userPost.setCountry(country);
            userPost.setHashTags(userPostDto.getHashTagIds());
            userPost.setBrand(brand);
            userPost.setThumbnail(userPostDto.getThumbnail());
            userPost.setCaption(caption); // Use the provided caption
            userPost.setTime(LocalDateTime.now());
            userPost.setType(userPostDto.getType());
            userPost.setPath(userPostDto.getPath());
            userPost.setShares(0);
            userPost.setFavorites(0);

            logger.info(userPost.toString());

            // Save the post to the database
            UserPost savedPost = userPostRepository.save(userPost);

            // Save the file to the file system
            String uploadPath = "C:\\Users\\user\\Documents\\explore\\exploredev\\Posts\\";
            file.transferTo(new File(uploadPath));

            logger.info("Post uploaded successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body("Post uploaded successfully");
        } catch(Exception e){
            e.printStackTrace();
            logger.error("Failed to upload post: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload post: " + e.getMessage());
        }
    }


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

    @Override
    public ResponseEntity<Object> checkPostOwnership(Long postId, Long profileId) {
        try {
            Optional<UserPost> optionalUserPost = userPostRepository.findById(postId);
            if (optionalUserPost.isPresent()) {
                UserPost userPost = optionalUserPost.get();
                if (userPost.getProfile().getId().equals(profileId)) {
                    // Profile owns the post
                    return ResponseEntity.status(HttpStatus.OK).body("Profile owns the post");
                } else {
                    // Profile does not own the post
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Profile does not own the post");
                }
            } else {
                // Post not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }
        } catch (Exception e) {
            // Handle exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to check post ownership: " + e.getMessage());
        }
    }
    @Override
    public ResponseEntity<Object> checkPostContentType(UserPostDto userPostDto) {
        try {
            Long postId = userPostDto.getId();

            // Fetch the post from the repository based on the postId
            Optional<UserPost> optionalUserPost = userPostRepository.findById(postId);

            if (optionalUserPost.isPresent()) {
                UserPost userPost = optionalUserPost.get();
                String postType = userPost.getType();

                if (postType.equalsIgnoreCase("video")) {
                    logger.info("Post with ID {} is a video.", postId);
                    return ResponseEntity.ok("Video");
                } else if (postType.equalsIgnoreCase("image")) {
                    logger.info("Post with ID {} is an image.", postId);
                    return ResponseEntity.ok("Image");
                } else {
                    logger.warn("Unknown post type for post with ID {}.", postId);
                    return ResponseEntity.ok("Unknown");
                }
            } else {
                logger.warn("Post with ID {} not found.", postId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }
        } catch (Exception e) {
            logger.error("Failed to check post content type: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to check post content type: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<Object> editCaption(UserPostDto userPostDto, Long profileId) {
        try {
            Long postId = userPostDto.getId();

            // Fetch the post from the repository based on the postId
            Optional<UserPost> optionalUserPost = userPostRepository.findById(postId);

            if (optionalUserPost.isPresent()) {
                UserPost userPost = optionalUserPost.get();

                // Check if the user is the owner of the post
                if (!userPost.getProfile().getId().equals(profileId)) {
                    logger.warn("User with profileId {} is not the owner of the post with ID {}", profileId, postId);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to edit this post.");
                }

                // Update the caption of the post
                userPost.setCaption(userPostDto.getCaption());
                userPostRepository.save(userPost);

                logger.info("Caption of the post with ID {} edited successfully by user with profileId {}", postId, profileId);
                return ResponseEntity.ok("Caption edited successfully.");
            } else {
                logger.warn("Post with ID {} not found", postId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }
        } catch (Exception e) {
            logger.error("Failed to edit post caption: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to edit post caption: " + e.getMessage());
        }
    }
}
