package com.example.demo.chatroom;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Repositories.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    private final ProfileRepository profileRepository;

    public ChatRoom getChatRoomId(Long senderId,Long recipientId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(
    senderId, recipientId, recipientId, senderId
);
        if(chatRoom.isPresent()){
            return chatRoom.get();
        }else{
            Profile recipient = profileRepository.findProfilesById(recipientId).get();
            Profile sender = profileRepository.findProfilesById(senderId).get();
            ChatRoom chatRoom3 = new ChatRoom();
            chatRoom3.setRecipient(recipient);
            chatRoom3.setSender(sender);
            ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom3);
            return savedChatRoom;
        }
    }
}
