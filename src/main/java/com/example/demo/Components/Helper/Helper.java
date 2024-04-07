package com.example.demo.Components.Helper;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class Helper {
    public String calculateTimeDifference(LocalDateTime givenTime) {
        // Get the current local time
        LocalDateTime currentTime = LocalDateTime.now();

        // Calculate the difference between the given time and the current time
        Duration duration = Duration.between(givenTime, currentTime);

        // Convert the duration to minutes, hours, or weeks
        long minutesDifference = duration.toMinutes();
        long hoursDifference = duration.toHours();
        long weeksDifference = duration.toDays() / 7;

        // Determine the appropriate time unit based on the magnitude of the difference
        if (weeksDifference > 0) {
            return weeksDifference + " weeks ago";
        } else if (hoursDifference > 0) {
            return hoursDifference + " hours ago";
        } else {
            return minutesDifference + " minutes ago";
        }
    }
}
