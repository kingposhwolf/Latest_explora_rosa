package com.example.demo.Services.ProfileService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface ProfileService {

    ResponseEntity<Object> updateProfile(MultipartFile proFilePicture, Long profileId, MultipartFile coverPhoto, String bio, String address);
}
