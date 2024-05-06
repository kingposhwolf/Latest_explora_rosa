package com.example.demo.Components.Helper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Repositories.CommentLikeRepository;
import com.example.demo.Repositories.FavoritesRepository;
import com.example.demo.Repositories.LikeRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class Helper {
    private final LikeRepository likeRepository;

    private final FavoritesRepository favoritesRepository;

    private final CommentLikeRepository commentLikeRepository;

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

    public List<Map<String, Object>> mapTimer(List<Map<String, Object>> data, Long profileId) {
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
                    Optional<Long> liked = commentLikeRepository.findIfLikeComment(commentId, profileId);
                    if(liked.isPresent()){
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

    public Map<String, Object> mapSingleTimer(Map<String, Object> data) {
        // Create a new map with the existing entries except timestamp
        Map<String, Object> modifiedData = new HashMap<>(data);
        modifiedData.remove("timestamp");

        // Calculate the time difference and add it to the map
        LocalDateTime timestamp = (LocalDateTime) data.get("timestamp");

        String timeDifference = calculateTimeDifference(timestamp);

        modifiedData.put("duration", timeDifference);

        return modifiedData;
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
        return data.stream()
                .map(post -> {
                    // Create a new map with the existing entries except timestamp
                    Map<String, Object> modifiedPost = new HashMap<>(post);
                    modifiedPost.remove("timestamp");

                    // Calculate the time difference and add it to the map
                    LocalDateTime timestamp = (LocalDateTime) post.get("timestamp");
                    String timeDifference = calculateTimeDifference(timestamp);

                    modifiedPost.put("duration", timeDifference);
                    modifiedPost.put("showShare", false);

                    //Check if the user like that post
                    Long postId = (Long) post.get("id");
                    Optional<Long> liked = likeRepository.findIfLikePost(postId, profileId);
                    if(liked.isPresent()){
                        modifiedPost.put("liked", true);
                    }else{
                        modifiedPost.put("liked", false);
                    }

                    //check if the user add the post to the favorites
                    Optional<Long> favorited = favoritesRepository.findFavoriteByPostAndUser(postId, profileId);
                    if(favorited.isPresent()){
                        modifiedPost.put("favorite", true);
                    }else{
                        modifiedPost.put("favorite", false);
                    }

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
    
}
