package com.example.demo.userChat;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.InputDto.ChatUser;
import com.example.demo.Models.UserManagement.Profile;
// import com.example.demo.Models.UserManagement.User;
import com.example.demo.Models.UserManagement.Management.Status;
import com.example.demo.Repositories.ProfileRepository;
// import com.example.demo.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService2 {
    // private final UserRepository repository;
    private final ProfileRepository profileRepository;

    public void saveUser(ChatUser user) {
        Optional<Profile> profileOpt = profileRepository.findProfilesById(user.getUsername());
        if(profileOpt.isPresent()){
            Profile profile = profileOpt.get();
            profile.setStatus(Status.ONLINE);
            profileRepository.save(profile);
        }
    }

    @SuppressWarnings("null")
    public void disconnect(ChatUser user) {
        Optional<Profile> profileOpt = profileRepository.findProfilesById(user.getUsername());
        if(profileOpt.isPresent()){
            Profile profile = profileOpt.get();
            profile.setStatus(Status.OFFLINE);
            profileRepository.save(profile);
        }
    }

    public List<Profile> findConnectedUsers() {
        return profileRepository.findByStatus(Status.ONLINE);
    }
}
