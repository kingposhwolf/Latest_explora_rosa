// package com.example.demo.Services.SpotifyService;

// // import com.example.demo.Config.SpotifyConfiguration;
// import com.example.demo.Models.Spotify.SpotifySong;
// import lombok.AllArgsConstructor;
// import org.apache.hc.core5.http.ParseException;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// // import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;
// import se.michaelthelin.spotify.SpotifyApi;
// import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
// import se.michaelthelin.spotify.model_objects.specification.Paging;
// import se.michaelthelin.spotify.model_objects.specification.Track;
// import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
// import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

// import java.io.IOException;

// @AllArgsConstructor
// @Service
// public class SpotifyServiceImpl implements SpotifyService{

//     private static final Logger logger = LoggerFactory.getLogger(SpotifyServiceImpl.class);


//     private final SpotifyApi spotifyApi;

//     // @Autowired
//     // public SpotifyServiceImpl(SpotifyConfiguration spotifyConfiguration) {
//     //     this.spotifyApi = spotifyConfiguration.getSpotifyObject();
//     // }

//     @Override
//     public ResponseEntity<SpotifySong> searchSong(String query) {
//         try {
//             // Make a request to the Spotify API to search for tracks by name or artist name
//             SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(query).build();
//             Paging<Track> trackPaging = searchTracksRequest.execute();

//             // Get the first track from the search results
//             Track track = trackPaging.getItems()[0]; // Assuming we always get at least one result

//             // Map the response to your SpotifySong DTO
//             SpotifySong spotifySong = mapToSpotifySong(track);

//             // Return ResponseEntity with OK status and the SpotifySong DTO
//             return ResponseEntity.ok(spotifySong);
//         } catch (IOException | SpotifyWebApiException e) {
//             // Handle exceptions
//             e.printStackTrace(); // Log the exception
//             // Return ResponseEntity with Internal Server Error status and appropriate message
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//         }catch (ParseException e) {
//             // Handle ParseException
//             e.printStackTrace(); // Log the exception
//             // Return ResponseEntity with Bad Request status and appropriate message
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//         }
//     }
//     @Override
//     public ResponseEntity<SpotifySong> getSongById(String songId) {
//         try {
//             // Make a request to the Spotify API to get information about the song with the specified ID
//             GetTrackRequest trackRequest = spotifyApi.getTrack(songId).build();
//             Track track = trackRequest.execute();

//             // Map the response to your SpotifySong DTO
//             SpotifySong spotifySong = mapToSpotifySong(track);

//             // Return ResponseEntity with OK status and the SpotifySong DTO
//             return ResponseEntity.ok(spotifySong);
//         } catch (IOException | SpotifyWebApiException e) {
//             // Handle exceptions
//             logger.error("Fetching song by Id failed!" + e.getMessage());
//             // Return ResponseEntity with Internal Server Error status and appropriate message
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//         }catch (ParseException e) {
//             // Handle ParseException
//             e.printStackTrace(); // Log the exception
//             // Return ResponseEntity with Bad Request status and appropriate message
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//         }
//     }

//     // Helper method to map the Spotify Track object to your SpotifySong DTO
//     private SpotifySong mapToSpotifySong(Track track) {
//         SpotifySong spotifySong = new SpotifySong();
//         spotifySong.setId(track.getId());
//         spotifySong.setName(track.getName());
//         spotifySong.setArtist(track.getArtists()[0].getName()); // Assuming only one artist is present
//         spotifySong.setAlbum(track.getAlbum().getName());
//         spotifySong.setDurationMillis(track.getDurationMs());
//         spotifySong.setAlbumArtUrl(track.getAlbum().getImages()[0].getUrl()); // Assuming the first image is the album art
//         // Set default values for start and end time
//         spotifySong.setStartTimeSeconds(0);
//         spotifySong.setEndTimeSeconds(spotifySong.getDurationSeconds());
//         return spotifySong;
//     }

// }
