package com.example.demo.Components.Helper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Helper {
    public Long calculateTimeDifference(LocalDateTime givenTime) {
        // Get the current local time
        LocalDateTime currentTime = LocalDateTime.now();

        // Calculate the difference between the given time and the current time
        Duration duration = Duration.between(givenTime, currentTime);

        // Convert the duration to minutes, hours, days, or weeks
        long secondDifference = duration.toSeconds();
        return secondDifference;
        // long hoursDifference = duration.toHours();
        // long daysDifference = duration.toDays();

        // Determine the appropriate time unit based on the magnitude of the difference
        // if (daysDifference >= 7) {
        // long weeksDifference = daysDifference / 7;
        // return weeksDifference + (weeksDifference > 1 ? " weeks ago" : " week ago");
        // } else if (daysDifference > 0) {
        // return daysDifference + (daysDifference > 1 ? " days ago" : " day ago");
        // } else if (hoursDifference > 0) {
        // return hoursDifference + (hoursDifference > 1 ? " hours ago" : " hour ago");
        // } else {
        // return minutesDifference + (minutesDifference > 1 ? " minutes ago" : " minute
        // ago");
        // }
    }

    public List<Map<String, Object>> mapTimer(List<Map<String, Object>> data) {
        return data.stream()
                .map(post -> {
                    // Create a new map with the existing entries except timestamp
                    Map<String, Object> modifiedPost = new HashMap<>(post);
                    modifiedPost.remove("timestamp");

                    // Calculate the time difference and add it to the map
                    LocalDateTime timestamp = (LocalDateTime) post.get("timestamp");
                    Long timeDifference = calculateTimeDifference(timestamp);
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

        long timeDifference = calculateTimeDifference(timestamp);

        modifiedData.put("duration", timeDifference);

        return modifiedData;
    }

    public List<Map<String, Object>> postMapTimer(List<Map<String, Object>> data) {
        return data.stream()
                .map(post -> {
                    // Create a new map with the existing entries except timestamp
                    Map<String, Object> modifiedPost = new HashMap<>(post);
                    modifiedPost.remove("timestamp");

                    // Calculate the time difference and add it to the map
                    LocalDateTime timestamp = (LocalDateTime) post.get("timestamp");
                    Long timeDifference = calculateTimeDifference(timestamp);
                    modifiedPost.put("duration", timeDifference);
                    modifiedPost.put("showComment", false);
                    modifiedPost.put("showShare", false);

                    return modifiedPost;
                })
                .collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    public String imageUlr(MultipartFile proFilePicture, Long profileId) {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            String folderPath = "src\\main\\resources\\static\\profileImg\\";
            // Generate a unique filename for the profile picture
            String fileName = "profile_picture_" + profileId + "_" + currentTime.getYear() + "_"
                    + currentTime.getMonthValue() + "_" + currentTime.getDayOfMonth() + "_" + currentTime.getHour()
                    + "_" + currentTime.getMinute() + "_" + currentTime.getSecond();

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

            // Return the recorded filename
            return recordedFileName;
        } catch (Exception e) {
            return "";
        }
    }

}
