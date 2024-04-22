package com.example.demo.chatroom;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.chatroom.Repository.ChatRoomRepository;
import com.example.demo.chatroom.Repository.GroupChatRepository;
import com.example.demo.chatroom.Repository.GroupChatRoomRepository;
import com.example.demo.chatroom.Repository.PersonalChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    private final ProfileRepository profileRepository;

    private final PersonalChatRoomRepository personalChatRoomRepository;

    private final GroupChatRoomRepository groupChatRoomRepository;

    private final GroupChatRepository groupChatRepository;

    public ChatRoom getChatRoomId(Long senderId,Long recipientId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(
    senderId, recipientId, recipientId, senderId);

        if(chatRoom.isPresent()){
            return chatRoom.get();
        }else{
            Profile recipient = profileRepository.findProfilesById(recipientId).get();
            Profile sender = profileRepository.findProfilesById(senderId).get();
            PersonalChatRoom personalChatRoom = new PersonalChatRoom();
            personalChatRoom.setRecipient(recipient);
            personalChatRoom.setSender(sender);
            
            PersonalChatRoom savedChatRoom = personalChatRoomRepository.save(personalChatRoom);
            return savedChatRoom;
        }
    }

    public ChatRoom getGroupChatRoomId(Long senderId,Long recipientId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderIdAndGroup(senderId, recipientId);
        if(chatRoom.isPresent()){
            return chatRoom.get();
        }else{
            GroupChat recipient = groupChatRepository.findGroupChatIdById(recipientId).get();
            Profile sender = profileRepository.findProfilesById(senderId).get();
            GroupChatRoom groupChatRoom = new GroupChatRoom();
            groupChatRoom.setGroupRecipient(recipient);
            groupChatRoom.setSender(sender);
            
            GroupChatRoom savedChatRoom = groupChatRoomRepository.save(groupChatRoom);
            return savedChatRoom;
        }
    }
}
