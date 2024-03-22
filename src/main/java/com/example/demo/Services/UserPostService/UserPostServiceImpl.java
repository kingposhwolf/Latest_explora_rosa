package com.example.demo.Services.UserPostService;
import com.example.demo.InputDto.UserPostDto;
import com.example.demo.Models.*;
import com.example.demo.Repositories.*;

import lombok.AllArgsConstructor;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserPostServiceImpl implements UserPostService{
    private static final Logger logger = LoggerFactory.getLogger(UserPostServiceImpl.class);

    private final String postfolderPath="C:\\Users\\user\\Documents\\explore\\exploredev\\userPosts\\";

    private final UserPostRepository userPostRepository;
    private final ProfileRepository profileRepository;
    private final CountryRepository countryRepository;
    private final HashTagRepository hashTagRepository;
    private final BrandRepository brandRepository;
   // private final LikeRepository likeRepository;

@Override
@Transactional
public ResponseEntity<Object> uploadPost(
        UserPostDto userPostDto,
        MultipartFile[] files,
        Long profileId,
        String caption,
        Long brandId,
        List<String> hashtagNames) throws IOException {
    try {
        if (files.length > 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Exceeded maximum file limit (6)");
        }
        else{
            String folderPath= "C:\\Users\\user\\Documents\\explore\\exploredev\\userPosts\\";

            // Generate the filename using profileId, time, day, month, and year
            LocalDateTime currentTime = LocalDateTime.now();
            String[] fileNames = new String[files.length]; // Array to store file names



            // Fetch profile and country entities
            Optional<Profile> profile = profileRepository.findById(profileId);
            if(profile.isEmpty()){
                return ResponseEntity.status(400).body("Profile does not exist");
            }

            List<HashTag> hashTags = new ArrayList<>();
            for (String hashtagName : hashtagNames) {
                if (hashtagName != null && !hashtagName.isEmpty()) { // Check for null and empty strings
                    Optional<HashTag> optionalHashTag = hashTagRepository.findByName(hashtagName);
                    HashTag hashTag;
                    if (optionalHashTag.isPresent()) {
                        hashTag = optionalHashTag.get();
                    } else {
                        // Create a new hashtag if it doesn't exist
                        hashTag = new HashTag();
                        hashTag.setName(hashtagName);
                        try {
                            hashTag = hashTagRepository.save(hashTag); // Save the new hashTag to get the ID
                        } catch (Exception e) {
                            // Handle any exceptions
                            e.printStackTrace(); // Print stack trace for debugging
                            logger.error("Error in creating hashTag");
                        }
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


            if(files == null || files.length == 0){
                throw new IllegalArgumentException("No files uploaded");
            }
            else {
                // Initialize a list to hold recorded filenames
                List<String> recordedFileNames = new ArrayList<>();

                //Initialize a list to hold content Types
                List<String> filesContentTypes = new ArrayList<>();

                // Iterate over each file in the array
                for (int n = 0; n < files.length; n++) {
                    MultipartFile file = files[n];
                    if (file.isEmpty()) {
                        throw new IllegalArgumentException("File at index " + n + " is empty");
                    }

                    String fileName = "post_" + profileId + "_" + currentTime.getYear() + "_" + currentTime.getMonthValue() + "_" + currentTime.getDayOfMonth() + "_" + currentTime.getHour() + "_" + currentTime.getMinute() + "_" + currentTime.getSecond() + "_" + (n + 1);


                    // Check if the content type is allowed
                    String contentType = file.getContentType();
                    if (!isValidContentType(contentType)) {
                        logger.error("Unsupported content type: {}", contentType);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported content type: " + contentType);
                    }
                    String fileContentType =  contentType + "content_" + (n+1);

                    // Obtaining the Original file name
                    String originalFileName = file.getOriginalFilename();

                    // Create a variable to store the file extension
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                    // Directory path where you want to save ;
                    String insPath = folderPath + fileName + fileExtension;

                    //Name of the single file to be stored in a file system
                    String recordedFileName = fileName + fileExtension;

                    // Re-writing the file again so that we can save it using our new name
                    byte[] bytes = file.getBytes();
                    Files.write(Paths.get(insPath), bytes);

                    fileNames[n] = recordedFileName;

                    // Add recorded filename to the recorded filename list
                    recordedFileNames.add(recordedFileName);
                    //Add file content type to the file content types list
                    filesContentTypes.add(fileContentType);

                }
                userPostDto.setNames(recordedFileNames);
                userPostDto.setContentTypes(filesContentTypes);


                try {
                    // Save the user post
                    UserPost userPost = new UserPost();
                    userPost.setProfile(profile.get());
                    userPost.setNames(userPostDto.getNames());
                    userPost.setCountry(profile.get().getCountry());
                    userPost.setHashTags(userPostDto.getHashTagIds());
                    userPost.setBrand(brand);
                    userPost.setThumbnail(userPostDto.getThumbnail());
                    userPost.setCaption(caption); // Use the provided caption
                    userPost.setTime(LocalDateTime.now());
                    userPost.setContentTypes(userPostDto.getContentTypes());
                    userPost.setPath(userPostDto.getPath());
                    userPost.setShares(0);
                    userPost.setFavorites(0);

                    //Print to see what's being carried
                    logger.info(userPost.toString());

                    // Save the post to the database

                    UserPost savedPost = userPostRepository.save(userPost);

                    // Transfer the file to the specified path
                    // file.transferTo(new File(uploadPath));

                    logger.info("Post uploaded successfully");
                    return ResponseEntity.status(HttpStatus.CREATED).body("Post uploaded successfully");
                } catch (Exception e){
                    throw new IOException("Failed to upload file: " + e.getMessage());

                }
            }

        }


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

    @Override
    @Transactional
    public ResponseEntity<Object> uploadSnippet(
            UserPostDto userPostDto,
            MultipartFile videoSnippet,
            MultipartFile thumbNail,
            Long profileId,
            String caption,
            Long brandId,
            List<String> hashtagNames) throws IOException {


            try{
                // Path of Snippets
                String videoSnippetsPath= "C:\\Users\\user\\Documents\\explore\\exploredev\\videoSnippets\\";

                //Initialize a list to hold content Types
                List<String> filesContentTypes = new ArrayList<>();

                //Initialize a list to hold a name of snippet
                List<String> snippetName = new ArrayList<>();

                // Check if the content type is allowed
                String contentType = videoSnippet.getContentType();
                if (!isValidContentTypeForSnippet(contentType)) {
                    logger.error("Unsupported content type: {}", contentType);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported content type: " + contentType);
                }

                //Overwrite the content type for our personal usage on post retreiveing algorithm
               String  snippetContentType = "snippet";
                filesContentTypes.add(snippetContentType);
                userPostDto.setContentTypes(filesContentTypes);

                // Fetch profile and country entities
                Optional<Profile> profile = profileRepository.findById(profileId);
                if(profile.isEmpty()){
                    return ResponseEntity.status(400).body("Profile does not exist");
                }

                // Fetch brand entity
                Brand brand = null;
                userPostDto.setBrandId(brandId);
                if (userPostDto.getBrandId() != null) {
                    brand = brandRepository.findById(brandId)
                            .orElseThrow(() -> new IllegalArgumentException("Brand with ID " + userPostDto.getBrandId() + " not found"));
                }

                // Check if the video snippet duration is within the time limit
                Duration duration = getVideoDuration(videoSnippet);
                if (duration != null && duration.getSeconds() > 60) {
                    logger.error("Video snippet duration exceeds the time limit.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Video snippet duration exceeds the time limit.");
                }


                // Fetch HashTags
                List<HashTag> snippetHashTags = new ArrayList<>();
                for (String hashtagName : hashtagNames) {
                    if (hashtagName != null && !hashtagName.isEmpty()) { // Check for null and empty strings
                        Optional<HashTag> optionalHashTag = hashTagRepository.findByName(hashtagName);
                        HashTag hashTag;
                        if (optionalHashTag.isPresent()) {
                            hashTag = optionalHashTag.get();
                        } else {
                            // Create a new hashtag if it doesn't exist
                            hashTag = new HashTag();
                            hashTag.setName(hashtagName);
                            try {
                                hashTag = hashTagRepository.save(hashTag); // Save the new hashTag to get the ID
                            } catch (Exception e) {
                                // Handle any exceptions
                                e.printStackTrace(); // Print stack trace for debugging
                                logger.error("Error in creating hashTag");
                            }
                        }
                        snippetHashTags.add(hashTag);
                    }
                }
                // Set the list of hashtags to the userPostDto
                userPostDto.setHashTagIds(snippetHashTags);

                // Generate snippet name based on id and current time of posting;  rename the file before saving
                LocalDateTime currentTime = LocalDateTime.now();

                String videoSnippetGeneratedName = "snippet_" + profileId + "_" + currentTime.getYear() + "_" + currentTime.getMonthValue() + "_" + currentTime.getDayOfMonth() + "_" + currentTime.getHour() + "_" + currentTime.getMinute() + "_" + currentTime.getSecond();

                // Obtain the original snippet Name
                String videoOriginalName = videoSnippet.getOriginalFilename();

                //Obtain the snippet Extension
                String videoSnippetExtension = videoOriginalName.substring(videoOriginalName.lastIndexOf("."));

                //Name a single file to be stored in file system
                String videoSnippetName = videoSnippetGeneratedName + videoSnippetExtension;
                //Add the name of single file to a list to be fed to the database
                snippetName.add(videoSnippetName);

                String insPath = videoSnippetsPath + videoSnippetGeneratedName + videoSnippetExtension;


                //Generate thumbnail Name
                String thumbNailGeneratedName = "thumbnail_" +  profileId + "_" + currentTime.getYear() + "_" + currentTime.getMonthValue() + "_" + currentTime.getDayOfMonth() + "_" + currentTime.getHour() + "_" + currentTime.getMinute() + "_" + currentTime.getSecond();

                //Obtain original file name
                String thumbNailOriginalName = thumbNail.getOriginalFilename();

                //Obtain thumbnail extension
                String thumbNailExtension = thumbNailOriginalName.substring(thumbNailOriginalName.lastIndexOf("."));

                //Obtain thumbPath
                String thumbPath = videoSnippetsPath + thumbNailGeneratedName + thumbNailExtension;
                userPostDto.setThumbnail(thumbPath);

                try {
                    // Re-writing the file again so that we can save it using our new name
                    byte[] bytes = videoSnippet.getBytes();
                    Files.write(Paths.get(insPath), bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("Failed to write the snippet to the specified path: {}", e.getMessage());

                }
                try{
                    //Re-writing the thumbNail with a new generated name
                    byte[] thumbBytes = thumbNail.getBytes();
                    Files.write(Paths.get(thumbPath), thumbBytes);

                }catch (IOException e){
                    e.printStackTrace();
                    logger.error("Failed to write the thumbNail to the specified path: {}", e.getMessage());
                }

                // Save the snippet as a normal post
                UserPost userPost = new UserPost();
                userPost.setProfile(profile.get());
                userPost.setNames(userPostDto.getNames());
                userPost.setCountry(profile.get().getCountry());
                userPost.setHashTags(userPostDto.getHashTagIds());
                userPost.setBrand(brand);
                //Save the path of thumbPath onto the thumbnail attribute
                userPost.setThumbnail(userPostDto.getThumbnail());
                //Continue filling other fields
                userPost.setCaption(caption); // Use the provided caption
                userPost.setTime(LocalDateTime.now());
                userPost.setContentTypes(userPostDto.getContentTypes());
                userPost.setPath(userPostDto.getPath());
                userPost.setShares(0);
                userPost.setFavorites(0);

                //Print to see what's being carried
                logger.info(userPost.toString());

                // Save the post to the database

                UserPost savedPost = userPostRepository.save(userPost);

                return ResponseEntity.status(HttpStatus.CREATED).body("Snippet uploaded successfully");


            }catch (Exception e){
                e.printStackTrace();
                logger.error("Failed to upload the Snippet:{}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload snippet: " + e.getMessage());

            }

    }
    private boolean isValidContentTypeForSnippet(String contentType) {
        // Check if the content type is not null and matches the allowed types
        return contentType != null && (contentType.startsWith("video/"));
    }

    // Method to retrieve the duration of the video snippet
    private Duration getVideoDuration(MultipartFile videoSnippet) throws IOException {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoSnippet.getInputStream())) {
            grabber.start();
            double durationInSeconds = grabber.getLengthInTime() / 1000000.0;
            return Duration.ofSeconds((long) durationInSeconds);
        } catch (FrameGrabber.Exception e) {
            logger.error("Error while retrieving video duration: {}", e.getMessage());
            throw new IOException("Error while retrieving video duration: " + e.getMessage());
        }
    }

    private String processThumbnail(MultipartFile files, MultipartFile thumbnailFile, Long profileId) throws IOException {
        // Generate a variable for a current time
        LocalDateTime currentTime = LocalDateTime.now();

        // Generate a unique filename for the thumbnail
        String thumbnailFileName = "thumbnail_" +  profileId + "_" + currentTime.getYear() + "_" + currentTime.getMonthValue() + "_" + currentTime.getDayOfMonth() + "_" + currentTime.getHour() + "_" + currentTime.getMinute() + "_" + currentTime.getSecond();

        // Define the directory where thumbnails will be stored (same as posts directory)
        String thumbnailDirectory = postfolderPath;

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
                ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", files.getOriginalFilename(), "-vframes", "1", "-an", "-s", "128x128", "-ss", "00:00:01", thumbnailFilePath);
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

// Validate thumbnail if the post is a video
//                    if (file.getContentType().equalsIgnoreCase("video")) {
//                        // Check if the user provided a thumbnail file
//                        MultipartFile thumbnailFile = userPostDto.getThumbnailFile();
//                        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
//                            String thumbnail = null;
//                            thumbnail = processThumbnail(files, thumbnailFile, profileId);
//                            userPostDto.setThumbnail(thumbnail);
//                        } else {
//                            String thumbnail = null;
//                            // Use Fmpeg to extract a frame from the video and save it as a thumbnail
//                            thumbnail = processThumbnail(files, null, profileId);
//                            userPostDto.setThumbnail(thumbnail);
//                        }
//                    }







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
//=======
//        // Check if the content type is allowed
//        String contentType = file.getContentType();
//        if (!isValidContentType(contentType)) {
//            logger.error("Unsupported content type: {}", contentType);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported content type: " + contentType);
//        }
//        userPostDto.setType(contentType);
//
//        // Fetch profile and country entities
//        Profile profile = profileRepository.findById(profileId)
//                .orElseThrow(() -> new IllegalArgumentException("Profile with ID " + profileId + " not found"));
//        Country country = countryRepository.findById(userPostDto.getCountryId())
//                .orElseThrow(() -> new IllegalArgumentException("Country with ID " + userPostDto.getCountryId() + " not found"));
//        userPostDto.setProfileId(profileId);
//
//        //Fetch or create new hashTag
//        List<HashTag> hashTags = new ArrayList<>();
//        for (String hashtagName : hashtagNames) {
//            if (hashtagName != null) {
//                Optional<HashTag> optionalHashTag = hashTagRepository.findByName(hashtagName);
//                HashTag hashTag = null;
//                if (optionalHashTag.isPresent()) {
//                    hashTag = optionalHashTag.get();
//                } else {
//                    // Create a new hashtag if it doesn't exist
//                    hashTag = new HashTag();
//                    hashTag.setName(hashtagName);
//                    hashTagRepository.save(hashTag); // Save the new hash tag to get the ID
//                }
//                hashTags.add(hashTag);
//            }
//        }
//        // Set the list of hashtags to the userPostDto
//        userPostDto.setHashTagIds(hashTags);
//>>>>>>> 981a58939b6028fd40233569b1530c43dba68d89
//
//


//        Country country = countryRepository.findById(userPostDto.getCountryId())
//                .orElseThrow(() -> new IllegalArgumentException("Country with ID " + userPostDto.getCountryId() + " not found"));
//        userPostDto.setProfileId(profileId);

//        //Fetch or create new hashTag
//        List<HashTag> hashTags = new ArrayList<>();
//        for (String hashtagName : hashtagNames) {
//            if (hashtagName != null) {
//                Optional<HashTag> optionalHashTag = hashTagRepository.findByName(hashtagName);
//                HashTag hashTag = null;
//                if (optionalHashTag.isPresent()) {
//                    hashTag = optionalHashTag.get();
//                } else {
//                    // Create a new hashtag if it doesn't exist
//                    hashTag = new HashTag();
//                    hashTag.setName(hashtagName);
////                    HashTagDto hashTagDto = new HashTagDto();
////                    hashTagDto.getName();
//                    hashTagRepository.save(hashTag); // Save the new hashTag to get the ID
//                }
//                hashTags.add(hashTag);
//            }
//        }
//        // Set the list of hashtags to the userPostDto
//        userPostDto.setHashTagIds(hashTags);
//            String uniqueFilename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//
//
//            // Ensure the directory exists, create it if necessary
//            File directory = new File(uploadPath);
//            if (!directory.exists()) {
//                directory.mkdirs(); // Create directory and any necessary parent directories
//            }
//
//
//                byte[] bytes = files.getBytes();
//                String originalFileName = files.getOriginalFilename();
//                // Create a variable to store the file extension
//                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
//
//                // Directory path where you want to save ;
//                String insPath = folderPath + fileName + fileExtension;
//
//                String recordedFileName = fileName + fileExtension;

//    @Override
//    public ResponseEntity<Object> checkPostContentType(UserPostDto userPostDto) {
//        try {
//            Long postId = userPostDto.getId();
//
//            // Fetch the post from the repository based on the postId
//            Optional<UserPost> optionalUserPost = userPostRepository.findById(postId);
//
//            if (optionalUserPost.isPresent()) {
//                UserPost userPost = optionalUserPost.get();
//                List<String> postType = userPost.getContentTypes();
//
//                if (postType.equalsIgnoreCase("video")) {
//                    logger.info("Post with ID {} is a video.", postId);
//                    return ResponseEntity.ok("Video");
//                } else if (postType.equalsIgnoreCase("image")) {
//                    logger.info("Post with ID {} is an image.", postId);
//                    return ResponseEntity.ok("Image");
//                } else {
//                    logger.warn("Unknown post type for post with ID {}.", postId);
//                    return ResponseEntity.ok("Unknown");
//                }
//            } else {
//                logger.warn("Post with ID {} not found.", postId);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
//            }
//        } catch (Exception e) {
//            logger.error("Failed to check post content type: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to check post content type: " + e.getMessage());
//        }
//    }
