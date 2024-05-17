package com.example.demo.Services.SpotifyService;

import com.example.demo.Models.Spotify.SpotifySong;
import org.springframework.http.ResponseEntity;

public interface SpotifyService {

    ResponseEntity<SpotifySong> searchSong(String query);

    ResponseEntity<SpotifySong> getSongById(String songId);
}
