package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

import java.net.URI;

@Service
public class SpotifyConfiguration {
    @Value("${spotify.api.client-id}")
    private String clientId;

    @Value("${spotify.api.client-secret}")
    private String clientSecret;

    @Value("${spotify.api.redirect-uri}")
    private String redirectUri;

    public SpotifyApi getSpotifyObject() {
        URI redirectedURL = SpotifyHttpManager.makeUri(redirectUri);

        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectedURL)
                .build();
    }
}
