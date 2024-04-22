package com.example.demo.Services.GroupChatService;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.InputDto.AddGroupMembersDto;
import com.example.demo.InputDto.NewGroupDto;
import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.chatroom.GroupChat;
import com.example.demo.chatroom.Repository.GroupChatRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupChatService {
    private static final Logger logger = LoggerFactory.getLogger(GroupChatService.class);

    private final GroupChatRepository groupChatRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public ResponseEntity<Object> createGroup(NewGroupDto newGroupDto) {
        try {
            GroupChat group = new GroupChat();

        for (Long id : newGroupDto.getMembers()) {
            Optional<Profile> profileOpt = profileRepository.findProfilesById(id);
            if(profileOpt.isPresent()){
                group.getMembers().add(profileOpt.get());
            }
        }

        group.setName(newGroupDto.getName());
        group.setDescription(newGroupDto.getDescription());
        
        GroupChat savedGroupChat = groupChatRepository.save(group);

        return ResponseEntity.status(201).body(savedGroupChat);
        } catch (Exception exception) {
            logger.error("Failed to create new Group chat : "+ exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @Transactional
    public ResponseEntity<Object> addMemberToGroup(AddGroupMembersDto addGroupMembersDto) {
        try {
            Optional<GroupChat> optionalGroup = groupChatRepository.findById(addGroupMembersDto.getGroupId());

        if (optionalGroup.isPresent()) {
            GroupChat group = optionalGroup.get();

            for (Long id : addGroupMembersDto.getMembers()) {
                Optional<Profile> profileOpt = profileRepository.findProfilesById(id);
                if(profileOpt.isPresent()){
                    if(!group.getMembers().contains(profileOpt.get())){
                        group.getMembers().add(profileOpt.get());
                    }
                }
            }
            GroupChat savedGroupChat = groupChatRepository.save(group);
            return ResponseEntity.status(200).body(savedGroupChat);
        }else{
            return ResponseEntity.status(400).body("The provided Group chat is not available");
        }
        } catch (Exception exception) {
            logger.error("Failed to add member(s) to Group chat : "+ exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @Transactional
    public ResponseEntity<Object> removeMemberFromGroup(AddGroupMembersDto addGroupMembersDto) {
        try {
            Optional<GroupChat> optionalGroup = groupChatRepository.findById(addGroupMembersDto.getGroupId());

        if (optionalGroup.isPresent()) {
            GroupChat group = optionalGroup.get();

            for (Long id : addGroupMembersDto.getMembers()) {
                Optional<Profile> profileOpt = profileRepository.findProfilesById(id);
                if(profileOpt.isPresent()){
                    if(group.getMembers().contains(profileOpt.get())){
                        group.getMembers().remove(profileOpt.get());
                    }
                }
            }

            GroupChat savedGroupChat = groupChatRepository.save(group);
            return ResponseEntity.status(200).body(savedGroupChat);
        } else {
            return ResponseEntity.status(400).body("The provided Group chat is not available");
        }
        } catch (Exception exception) {
            logger.error("Failed to remove member(s) to Group chat : "+ exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
}
