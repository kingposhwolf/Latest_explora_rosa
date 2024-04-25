package com.example.demo.Components.Helper;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Helper {
    public String calculateTimeDifference(LocalDateTime givenTime) {
        // Get the current local time
        LocalDateTime currentTime = LocalDateTime.now();
    
        // Calculate the difference between the given time and the current time
        Duration duration = Duration.between(givenTime, currentTime);
    
        // Convert the duration to minutes, hours, days, or weeks
        long minutesDifference = duration.toMinutes();
        long hoursDifference = duration.toHours();
        long daysDifference = duration.toDays();
    
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
    

    public List<Map<String, Object>> mapTimer(List<Map<String, Object>> data) {
        return data.stream()
                .map(post -> {
                    // Create a new map with the existing entries except timestamp
                    Map<String, Object> modifiedPost = new HashMap<>(post);
                    modifiedPost.remove("timestamp");
    
                    // Calculate the time difference and add it to the map
                    LocalDateTime timestamp = (LocalDateTime) post.get("timestamp");
                    String timeDifference = calculateTimeDifference(timestamp);
                    modifiedPost.put("duration", timeDifference);
    
                    return modifiedPost;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> postMapTimer(List<Map<String, Object>> data) {
        return data.stream()
                .map(post -> {
                    // Create a new map with the existing entries except timestamp
                    Map<String, Object> modifiedPost = new HashMap<>(post);
                    modifiedPost.remove("timestamp");
    
                    // Calculate the time difference and add it to the map
                    LocalDateTime timestamp = (LocalDateTime) post.get("timestamp");
                    String timeDifference = calculateTimeDifference(timestamp);
                    modifiedPost.put("duration", timeDifference);
                    modifiedPost.put("showComment", false);
                    modifiedPost.put("showShare", false);
    
                    return modifiedPost;
                })
                .collect(Collectors.toList());
    }
}
