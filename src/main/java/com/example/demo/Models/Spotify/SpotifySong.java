package com.example.demo.Models.Spotify;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotifySong {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("artist")
    private String artist;

    @JsonProperty("album")
    private String album;

    @JsonProperty("duration_ms")
    private int durationMillis; // Duration in milliseconds

    @JsonProperty("album_art_url")
    private String albumArtUrl; // URL to the album art image

    private int startTimeSeconds; // Starting time of the period in seconds
    private int endTimeSeconds; // Ending time of the period in seconds

    public int getDurationSeconds() {
        // Convert milliseconds to seconds
        return durationMillis / 1000;
    }

    public void setDurationSeconds(int durationSeconds) {
        // Convert seconds to milliseconds
        this.durationMillis = durationSeconds * 1000;
    }

}
