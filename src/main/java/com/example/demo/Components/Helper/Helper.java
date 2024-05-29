package com.example.demo.Components.Helper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Repositories.SocialMedia.Comment.CommentLikeRepository;
import com.example.demo.Repositories.SocialMedia.Favorite.FavoritesRepository;
import com.example.demo.Repositories.SocialMedia.Like.LikeRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class Helper {
    private final LikeRepository likeRepository;

    private final FavoritesRepository favoritesRepository;

    private final CommentLikeRepository commentLikeRepository;

    private static final int SEED_BOUND = 10000;

    public Long calculateChatTimeDifference(LocalDateTime givenTime) {
        // Get the current local time
        LocalDateTime currentTime = LocalDateTime.now();

        Duration duration = Duration.between(givenTime, currentTime);

        long secondDifference = duration.toSeconds();

        return secondDifference;
    }

    public String calculateTimeDifference(LocalDateTime givenTime) {
        // Get the current local time
        LocalDateTime currentTime = LocalDateTime.now();

        // Calculate the difference between the given time and the current time
        Duration duration = Duration.between(givenTime, currentTime);

        // Convert the duration to minutes, hours, days, or weeks
        long hoursDifference = duration.toHours();
        long daysDifference = duration.toDays();
        long minutesDifference = duration.toMinutes();

        // Determine the appropriate time unit based on the magnitude of the difference
        if (daysDifference >= 7) {
        long weeksDifference = daysDifference / 7;
        return weeksDifference + (weeksDifference > 1 ? " weeks ago" : " week ago");
        } else if (daysDifference > 0) {
        return daysDifference + (daysDifference > 1 ? " days ago" : " day ago");
        } else if (hoursDifference > 0) {
        return hoursDifference + (hoursDifference > 1 ? " hours ago" : " hour ago");
        } else {
        return minutesDifference + (minutesDifference > 1 ? " minutes ago" : " minute ago");
        }
    }

    public List<Map<String, Object>> mapCommentTimer(List<Map<String, Object>> data, Long profileId) {
        List<Long> commentsUSerLikes = commentLikeRepository.commentsUserLike(profileId);
        return data.stream()
                .map(post -> {
                    // Create a new map with the existing entries except timestamp
                    Map<String, Object> modifiedPost = new HashMap<>(post);
                    modifiedPost.remove("timestamp");

                    // Calculate the time difference and add it to the map
                    LocalDateTime timestamp = (LocalDateTime) post.get("timestamp");
                    String timeDifference = calculateTimeDifference(timestamp);
                    modifiedPost.put("duration", timeDifference);

                    Long commentId = (Long) post.get("id");

                    if(commentsUSerLikes.contains(commentId)){
                        modifiedPost.put("liked", true);
                    }else{
                        modifiedPost.put("liked", false);
                    }

                    return modifiedPost;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> mapChatTimer(List<Map<String, Object>> data) {
        return data.stream()
                .map(post -> {
                    // Create a new map with the existing entries except timestamp
                    Map<String, Object> modifiedPost = new HashMap<>(post);
                    modifiedPost.remove("timestamp");

                    // Calculate the time difference and add it to the map
                    LocalDateTime timestamp = (LocalDateTime) post.get("timestamp");
                    Long timeDifference = calculateChatTimeDifference(timestamp);
                    modifiedPost.put("duration", timeDifference);

                    return modifiedPost;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> singlePostMap(Map<String, Object> post, Long profileId) {
        List<Long> postUserLikes = likeRepository.postsUserLike(profileId);
        List<Long> favoritePosts = favoritesRepository.findPostByProfile(profileId);
    
        
                    // Create a new map with the existing entries except timestamp
                    Map<String, Object> modifiedPost = new HashMap<>(post);
                    modifiedPost.remove("timestamp");
    
                    // Get the timestamp from the post map and convert it to LocalDateTime
                    Timestamp timestamp = (Timestamp) post.get("timestamp");
                    LocalDateTime localDateTime = timestamp.toLocalDateTime();
    
                    // Calculate the time difference and add it to the map
                    String timeDifference = calculateTimeDifference(localDateTime);
                    modifiedPost.put("duration", timeDifference);
                    modifiedPost.put("showShare", false);
    
                    // Check if the user likes that post
                    Long postId = (Long) post.get("id");
                    modifiedPost.put("liked", postUserLikes.contains(postId));
    
                    // Check if the user added the post to the favorites
                    modifiedPost.put("favorite", favoritePosts.contains(postId));
    
                    // Parse hashtags
                    String hashTagsConcat = (String) post.get("hashTags");
                    List<Map<String, String>> hashTags = Arrays.stream(hashTagsConcat.split(","))
                            .map(pair -> {
                                String[] parts = pair.split(":");
                                Map<String, String> hashTag = new HashMap<>();
                                if (parts.length == 2) {
                                    hashTag.put("id", parts[0]);
                                    hashTag.put("name", parts[1]);
                                }
                                return hashTag;
                            })
                            .collect(Collectors.toList());
                    modifiedPost.put("hashTags", hashTags);
    
                    return modifiedPost;
    }

    public Map<String, Object> mapChatSingleTimer(Map<String, Object> data) {
        // Create a new map with the existing entries except timestamp
        Map<String, Object> modifiedData = new HashMap<>(data);
        modifiedData.remove("timestamp");

        // Calculate the time difference and add it to the map
        LocalDateTime timestamp = (LocalDateTime) data.get("timestamp");

        Long timeDifference = calculateChatTimeDifference(timestamp);

        modifiedData.put("duration", timeDifference);

        return modifiedData;
    }

    public List<Map<String, Object>> postMapTimer(List<Map<String, Object>> data, Long profileId) {
        List<Long> postUserLikes = likeRepository.postsUserLike(profileId);
        List<Long> favoritePosts = favoritesRepository.findPostByProfile(profileId);
    
        return data.stream()
                .map(post -> {
                    // Create a new map with the existing entries except timestamp
                    Map<String, Object> modifiedPost = new HashMap<>(post);
                    modifiedPost.remove("timestamp");
    
                    // Get the timestamp from the post map and convert it to LocalDateTime
                    Timestamp timestamp = (Timestamp) post.get("timestamp");
                    LocalDateTime localDateTime = timestamp.toLocalDateTime();
    
                    // Calculate the time difference and add it to the map
                    String timeDifference = calculateTimeDifference(localDateTime);
                    modifiedPost.put("duration", timeDifference);
                    modifiedPost.put("showShare", false);
    
                    // Check if the user likes that post
                    Long postId = (Long) post.get("id");
                    modifiedPost.put("liked", postUserLikes.contains(postId));
    
                    // Check if the user added the post to the favorites
                    modifiedPost.put("favorite", favoritePosts.contains(postId));
    
                    // Parse hashtags
                    String hashTagsConcat = (String) post.get("hashTags");
                    List<Map<String, String>> hashTags = Arrays.stream(hashTagsConcat.split(","))
                            .map(pair -> {
                                String[] parts = pair.split(":");
                                Map<String, String> hashTag = new HashMap<>();
                                if (parts.length == 2) {
                                    hashTag.put("id", parts[0]);
                                    hashTag.put("name", parts[1]);
                                }
                                return hashTag;
                            })
                            .collect(Collectors.toList());
                    modifiedPost.put("hashTags", hashTags);
    
                    return modifiedPost;
                })
                .collect(Collectors.toList());
    }
    
    

    @SuppressWarnings("null")
    public String saveImage(MultipartFile proFilePicture, Long profileId,String folderPath) {
        try {
            // Generate a unique filename for the profile picture
            String fileName = "file_" + profileId;

            // Obtain the original file name
            String originalFileName = proFilePicture.getOriginalFilename();

            // Create a variable to store the file extension
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // Directory path where you want to save the profile picture
            String imagePath = folderPath + fileName + fileExtension;

            // Name of the profile picture file to be stored in the file system
            String recordedFileName =  fileName + fileExtension;

            // Write the file to the file system
            byte[] bytes = proFilePicture.getBytes();
            Files.write(Paths.get(imagePath), bytes);

            return recordedFileName;
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteOldFile(String savedName,String folderPath) {
        // Construct the file path for the old profile picture
        String oldFilePath = folderPath + savedName;
    
        // Delete the old profile picture file
        File oldProfilePictureFile = new File(oldFilePath);
        if (oldProfilePictureFile.exists()) {
            oldProfilePictureFile.delete();
        }
    }


    public List<Map<String, Object>> mergeProfiles(List<Map<String, Object>> inputList) {
        // Map to store merged profiles based on profileId
        Map<Long, Map<String, Object>> mergedProfilesMap = new HashMap<>();
        
        // Iterate over the input list
        for (Map<String, Object> profile : inputList) {
            Long profileId = (Long) profile.get("profileId");
            String followedBy = (String) profile.get("followedBy");
            
            // Check if a profile with the same profileId already exists
            if (mergedProfilesMap.containsKey(profileId)) {
                // Merge the followedBy values
                Map<String, Object> existingProfile = mergedProfilesMap.get(profileId);
                String existingFollowedBy = (String) existingProfile.get("followedBy");
                existingProfile.put("followedBy", existingFollowedBy + " , " + followedBy);
            } else {
                // Add a new entry to the merged profiles map
                mergedProfilesMap.put(profileId, new HashMap<>(profile));
            }
        }
        
        // Convert the merged profiles map to a list
        List<Map<String, Object>> mergedList = new ArrayList<>(mergedProfilesMap.values());
        
        return mergedList;
    }


    public long generateSeed(int pageNumber) {
        // Use a page number to generate a unique seed for each page
        // You can use any method to generate the seed, this is just an example
        Random random = new Random(pageNumber * SEED_BOUND);
        return random.nextLong();
    }

    
}
