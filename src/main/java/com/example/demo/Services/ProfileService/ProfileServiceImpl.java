package com.example.demo.Services.ProfileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.ProfileDto;
import com.example.demo.Models.Profile;
import com.example.demo.Models.Title;
import com.example.demo.Models.User;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.Repositories.TitleRepository;
import com.example.demo.Repositories.UserRepository;

@Service
public class ProfileServiceImpl implements ProfileService{
    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository, UserRepository userRepository, TitleRepository titleRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Iterable<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> saveProfile(ProfileDto profileDto) {
        try{
           User user = userRepository.findById(profileDto.getUserId()).orElse(null);

           Title title = new Title();

           title.setId((long) 1);
           title.setName("USER");

    if (user == null) {
        String errorMessage = "Invalid input. User Id";
        return ResponseEntity.badRequest().body(errorMessage);
    }else{
        Profile profile = new Profile();

        profile.setBio(profileDto.getBio());
        profile.setFollowers(0);
        profile.setFollowing(0);
        profile.setPosts(0);
        profile.setTitle(null);
        profile.setPowerSize(0);
        profile.setTitle(title);
        profile.setUser(user);
    

    profileRepository.save(profile);
    
    logger.info("User Saved sucessfull" + user);

    return ResponseEntity.status(201).body("message: "+"profile created sucessfull");
    }
   
        }catch(Exception exception){
            logger.error("User saving failed" + exception.getMessage());
            return ResponseEntity.status(500).body("There is Problem at Our End");
        }
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> getProfileById(Long id) {
        try {
            Profile profile = profileRepository.findById(id).orElse(null);

            if (profile == null) {
                String errorMessage = "Invalid profile ID";
                return ResponseEntity.badRequest().body(errorMessage);
            }else{
                return ResponseEntity.status(200).body(profile);
            }
        } catch (Exception exception) {
            logger.error("User saving failed" + exception.getMessage());
            return ResponseEntity.status(500).body("There is Problem at Our End");
        }
    }

    // @Override
    // public Profile updatProfile(ProfileDto profileDto) {
        
    // }
}
