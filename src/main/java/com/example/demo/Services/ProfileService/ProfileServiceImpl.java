package com.example.demo.Services.ProfileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.GetProfileDto;
//import com.example.demo.Dto.ProfileDto;
import com.example.demo.Models.Profile;
//import com.example.demo.Models.Title;
//import com.example.demo.Models.User;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.Repositories.TitleRepository;
import com.example.demo.Repositories.UserRepository;

@Service
public class ProfileServiceImpl implements ProfileService{
    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);
    private final ProfileRepository profileRepository;
   // private final UserRepository userRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository, UserRepository userRepository, TitleRepository titleRepository) {
        this.profileRepository = profileRepository;
       // this.userRepository = userRepository;
    }

    @Override
    public Iterable<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    // @SuppressWarnings("null")
    // @Override
    // public ResponseEntity<Object> saveProfile(ProfileDto profileDto) {
    //     try{
    //        User user = userRepository.findById(profileDto.getId()).orElse(null);

    //        Title title = new Title();

    //        title.setId((long) 1);
    //        title.setName("USER");

    // if (user == null) {
    //     logger.error("Profile Saving failed Invalid User Information");
    //     return ResponseEntity.badRequest().body("Invalid User Infromation");
    // }else{
    //     Profile profile = new Profile();

    //     profile.setBio(profileDto.getBio());
    //     profile.setFollowers(0);
    //     profile.setFollowing(0);
    //     profile.setPosts(0);
    //     profile.setTitle(null);
    //     profile.setPowerSize(0);
    //     profile.setTitle(title);
    //     profile.setUser(user);
    

    // profileRepository.save(profile);
    
    // logger.info("\nProfile Created Successful: " + profile);

    // return ResponseEntity.status(201).body("profile created sucessfull");
    // }
    //     }catch(Exception exception){
    //         logger.error("Profile Saving Failed, Server Error: " + exception.getMessage());
    //         return ResponseEntity.status(500).body("Internal Server Error");
    //     }
    // }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> getProfileById(GetProfileDto getProfileDto) {
        try {
            Profile profile = profileRepository.findById(getProfileDto.getId()).orElse(null);

            if (profile == null) {
                logger.error("Failed to Fetch Profile Info, Invalid profile Id");
                return ResponseEntity.badRequest().body("Invalid profile ID");
            }else{
                logger.info("\nProfile Info Fetched Successful: " + profile);
                return ResponseEntity.status(200).body(profile);
            }
        } catch (Exception exception) {
            logger.error("\nProfile fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    // @Override
    // public Profile updatProfile(ProfileDto profileDto) {
        
    // }
}
