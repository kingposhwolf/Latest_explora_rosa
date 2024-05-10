package com.example.demo.Services.ProfileService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Components.Helper.Helper;
import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Repositories.UserManagement.AccountManagement.ProfileRepository;
import com.example.demo.Services.PersonalService.PersonalServiceImpl;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService{
    private static final Logger logger = LoggerFactory.getLogger(PersonalServiceImpl.class);

    private final ProfileRepository profileRepository;

    private final Helper helper;

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> updateProfile(MultipartFile proFilePicture, Long profileId, MultipartFile coverPhoto, String bio, String address) {
        try {
            Optional<Profile> profileOpt = profileRepository.findById(profileId);
            if(profileOpt.isPresent()){
                Profile profile = profileOpt.get();

                if(proFilePicture != null) {
                    if(profile.getProfilePicture() != null) {
                        helper.deleteOldFile(profile.getProfilePicture(), "src\\main\\resources\\static\\profileImg\\");
                    }
                    profile.setProfilePicture(helper.saveImage(proFilePicture, profileId,"src\\main\\resources\\static\\profileImg\\"));
                }

                if(coverPhoto != null) {
                    if(profile.getCoverPhoto() != null) {
                        helper.deleteOldFile(profile.getCoverPhoto(), "src\\main\\resources\\static\\coverImg\\");
                    }

                    profile.setCoverPhoto(helper.saveImage(coverPhoto, profileId,"src\\main\\resources\\static\\coverImg\\"));
                }

                if(bio != null && !bio.isEmpty()){
                    profile.setBio(bio);
                }

                if(address != null && !address.isEmpty()){
                    profile.setAddress(address);
                }

                profileRepository.save(profile);
                return ResponseEntity.status(200).body("Praise God");
            }else{
                return ResponseEntity.status(404).body("Profile not found");
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    
}
