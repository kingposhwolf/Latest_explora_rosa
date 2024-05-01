package com.example.demo.Services.ProfileService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.Services.PersonalService.PersonalServiceImpl;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService{
    private static final Logger logger = LoggerFactory.getLogger(PersonalServiceImpl.class);

    private final ProfileRepository profileRepository;

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> updateProfile(MultipartFile proFilePicture, Long profileId) {
        // , MultipartFile coverPhoto, String bio, String address
        try {
            if(proFilePicture == null || proFilePicture.isEmpty()) {
                return ResponseEntity.status(400).body("No file uploaded");
            } else {
                LocalDateTime currentTime = LocalDateTime.now();
                String folderPath = "src\\main\\resources\\static\\profileImg\\";
                // Generate a unique filename for the profile picture
                String fileName = "profile_picture_" + profileId + "_" + currentTime.getYear() + "_" + currentTime.getMonthValue() + "_" + currentTime.         getDayOfMonth() + "_" + currentTime.getHour() + "_" + currentTime.getMinute() + "_" + currentTime.getSecond();

                // Obtain the original file name
                String originalFileName = proFilePicture.getOriginalFilename();

                // Create a variable to store the file extension
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                // Directory path where you want to save the profile picture
                String imagePath = folderPath + fileName + fileExtension;

                // Name of the profile picture file to be stored in the file system
                String recordedFileName = "/profile/" + fileName + fileExtension;

                // Write the file to the file system
                byte[] bytes = proFilePicture.getBytes();
                Files.write(Paths.get(imagePath), bytes);
                
                // Return the recorded filename
               // return recordedFileName;

                Optional<Profile> profileOpt = profileRepository.findById(profileId);
                if(profileOpt.isPresent()){
                    Profile profile = profileOpt.get();
                    profile.setProfilePicture(recordedFileName);
                    profileRepository.save(profile);
                    return ResponseEntity.status(200).body("Praise God");
            }else{

                return ResponseEntity.status(404).body("Profile not found");
            }
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    
}
