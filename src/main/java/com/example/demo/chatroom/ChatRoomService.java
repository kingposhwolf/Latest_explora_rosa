package com.example.demo.chatroom;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom getChatRoomId(String senderId,String recipientId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(
    senderId, recipientId, recipientId, senderId
);
        if(chatRoom.isPresent()){
            return chatRoom.get();
        }else{
            ChatRoom chatRoom3 = new ChatRoom();
            chatRoom3.setRecipientId(recipientId);
            chatRoom3.setSenderId(senderId);
            ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom3);
            return savedChatRoom;
        }
    }
}
