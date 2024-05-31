package com.example.demo.Services.UserPostService;
import com.example.demo.Components.Helper.FFmpegUtils;
import com.example.demo.Components.Helper.Helper;
import com.example.demo.InputDto.SocialMedia.Post.UserPostDto;
import com.example.demo.Models.SocialMedia.BusinessPost;
import com.example.demo.Models.SocialMedia.HashTag;
import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Models.UserManagement.BussinessAccount.Brand;
import com.example.demo.Repositories.Information.Country.CountryRepository;
import com.example.demo.Repositories.SocialMedia.Content.BusinessPostRepository;
import com.example.demo.Repositories.SocialMedia.Content.UserPostRepository;
import com.example.demo.Repositories.SocialMedia.HashTag.HashTagRepository;
import com.example.demo.Repositories.UserManagement.AccountManagement.ProfileRepository;
import com.example.demo.Repositories.UserManagement.BusinessAccount.BrandRepository;

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
import java.math.BigDecimal;
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

    private final String postfolderPath="Posts";

    private final UserPostRepository userPostRepository;
    private final BusinessPostRepository businessPostRepository;
    private final ProfileRepository profileRepository;
    private final HashTagRepository hashTagRepository;
    private final BrandRepository brandRepository;
    private final CountryRepository countryRepository;
    private final Helper helper;
   // private final LikeRepository likeRepository;

@SuppressWarnings("null")
@Override
@Transactional
public ResponseEntity<Object> uploadPost(
        MultipartFile[] files,
        Long profileId,
        BigDecimal price,
        String caption,
        String location,
        Long brandId,
        List<String> hashtagNames,
        Long countryId) throws IOException {
            try {
                if (files.length > 6) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Exceeded maximum file limit (6)");
                }
            
                UserPostDto userPostDto = new UserPostDto();
                LocalDateTime currentTime = LocalDateTime.now();
                String folderPath = "src\\main\\resources\\static\\posts\\";
            
                Optional<Profile> profileOpt = profileRepository.findById(profileId);
                if (profileOpt.isEmpty()) {
                    return ResponseEntity.status(400).body("Profile does not exist");
                }
                Profile profile = profileOpt.get();
            
                List<HashTag> hashTags = getOrCreateHashTags(hashtagNames);
                userPostDto.setHashTagIds(hashTags);
            
                Brand brand = null;
                if (brandId != null) {
                    brand = brandRepository.findById(brandId)
                            .orElseThrow(() -> new IllegalArgumentException("Brand with ID " + brandId + " not found"));
                }
                userPostDto.setBrandId(brandId);
            
                if (files == null || files.length == 0) {
                    throw new IllegalArgumentException("No files uploaded");
                }
            
                List<String> recordedFileNames = new ArrayList<>();
                List<String> filesContentTypes = new ArrayList<>();
                Long duration = null;
            
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    if (file.isEmpty()) {
                        throw new IllegalArgumentException("File at index " + i + " is empty");
                    }
            
                    String fileName = generateFileName(profileId, currentTime, i);
                    String contentType = file.getContentType();
                    if (!isValidContentType(contentType)) {
                        logger.error("Unsupported content type: {}", contentType);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported content type: " + contentType);
                    }
            
                    String fileExtension = getFileExtension(file.getOriginalFilename());
                    String insPath = folderPath + fileName + fileExtension;
                    byte[] bytes = file.getBytes();
                    Files.write(Paths.get(insPath), bytes);
            
                    recordedFileNames.add(fileName + fileExtension);
                    filesContentTypes.add(contentType + "content_" + (i + 1));
            
                    if (contentType.startsWith("video/")) {
                        duration = FFmpegUtils.getVideoDuration(insPath);
                    }
                }
            
                userPostDto.setNames(recordedFileNames);
                userPostDto.setContentTypes(filesContentTypes);
            
                if(profile.getUser().getAccountType().getId() == 2){
                    if(price == null){
                        return ResponseEntity.status(400).body("Price is required for the business post");
                    }

                    BusinessPost businessPost = createBusinessPost(profile, countryId, hashTags, brand, caption, location, duration, userPostDto,price);
                    BusinessPost savedPost = userPostRepository.save(businessPost);

                    logger.info("Post uploaded successfully");
                return ResponseEntity.status(HttpStatus.CREATED).body(helper.singlePostMap(businessPostRepository.findBusinessPostDataById(savedPost.getId()), profileId));

                }else{
                    UserPost userPost = createUserPost(profile, countryId, hashTags, brand, caption, location, duration, userPostDto);
                    UserPost savedPost = userPostRepository.save(userPost);
                    logger.info("Post uploaded successfully");
                return ResponseEntity.status(HttpStatus.CREATED).body(helper.singlePostMap(userPostRepository.findUserPostDataById(savedPost.getId()), profileId));
                }
            
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Failed to upload post: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload post: " + e.getMessage());
            }
    }


    private boolean isValidContentType(String contentType) {
        // Check if the content type is not null and matches the allowed types
        return contentType != null && (contentType.startsWith("image/") || contentType.startsWith("video/"));
    }

    @SuppressWarnings("null")
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

                logger.info("Successful save post : " + savedPost);

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

    @SuppressWarnings("unused")
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


    @SuppressWarnings("null")

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

    @SuppressWarnings("null")
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

    // Helper methods
private List<HashTag> getOrCreateHashTags(List<String> hashtagNames) {
    List<HashTag> hashTags = new ArrayList<>();
    for (String hashtagName : hashtagNames) {
        if (hashtagName != null && !hashtagName.isEmpty()) {
            hashTags.add(hashTagRepository.findByName(hashtagName)
                    .orElseGet(() -> createNewHashTag(hashtagName)));
        }
    }
    return hashTags;
}

private HashTag createNewHashTag(String hashtagName) {
    HashTag hashTag = new HashTag();
    hashTag.setName(hashtagName);
    try {
        return hashTagRepository.save(hashTag);
    } catch (Exception e) {
        e.printStackTrace();
        logger.error("Error in creating hashTag");
        return hashTag;
    }
}

private String generateFileName(Long profileId, LocalDateTime currentTime, int index) {
    return String.format("post_%d_%d_%d_%d_%d_%d_%d_%d", profileId, currentTime.getYear(), currentTime.getMonthValue(),
            currentTime.getDayOfMonth(), currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond(), (index + 1));
}

private String getFileExtension(String originalFileName) {
    return originalFileName.substring(originalFileName.lastIndexOf("."));
}

private UserPost createUserPost(Profile profile, Long countryId, List<HashTag> hashTags, Brand brand, String caption,
                                String location, Long duration, UserPostDto userPostDto) throws IllegalArgumentException {
    UserPost userPost = new UserPost();
    userPost.setProfile(profile);
    userPost.setNames(userPostDto.getNames());
    userPost.setCountry(countryRepository.findById(countryId).orElseThrow(() -> new IllegalArgumentException("Country not found")));
    userPost.setHashTags(userPostDto.getHashTagIds());
    userPost.setBrand(brand);
    userPost.setThumbnail(userPostDto.getThumbnail());
    userPost.setCaption(caption);
    userPost.setTime(LocalDateTime.now());
    userPost.setContentTypes(userPostDto.getContentTypes());
    userPost.setPath("/posts");
    userPost.setShares(0);
    userPost.setFavorites(0);
    userPost.setLocation(location);
    userPost.setDuration(duration);
    logger.info(userPost.toString());
    return userPost;
}

private BusinessPost createBusinessPost(Profile profile, Long countryId, List<HashTag> hashTags, Brand brand, String caption,
                                String location, Long duration, UserPostDto userPostDto, BigDecimal price) throws IllegalArgumentException {
    BusinessPost userPost = new BusinessPost();
    userPost.setProfile(profile);
    userPost.setNames(userPostDto.getNames());
    userPost.setCountry(countryRepository.findById(countryId).orElseThrow(() -> new IllegalArgumentException("Country not found")));
    userPost.setHashTags(userPostDto.getHashTagIds());
    userPost.setBrand(brand);
    userPost.setThumbnail(userPostDto.getThumbnail());
    userPost.setCaption(caption);
    userPost.setTime(LocalDateTime.now());
    userPost.setContentTypes(userPostDto.getContentTypes());
    userPost.setPath("/posts");
    userPost.setShares(0);
    userPost.setFavorites(0);
    userPost.setLocation(location);
    userPost.setDuration(duration);
    userPost.setRate(BigDecimal.ZERO);
    userPost.setPrice(price);

    logger.info(userPost.toString());
    return userPost;
}
}