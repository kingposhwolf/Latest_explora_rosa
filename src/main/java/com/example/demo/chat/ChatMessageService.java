package com.example.demo.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.chatroom.ChatRoom;
import com.example.demo.chatroom.ChatRoomRepository;
import com.example.demo.chatroom.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRespository repository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        ChatRoom chatRoom = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId());

        chatMessage.setChatRoom(chatRoom);
        repository.save(chatMessage);
        return chatMessage;
        
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        
        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(
    senderId, recipientId, recipientId, senderId
);
        return repository.findByChatRoom(chatRoom.get()).orElse(new ArrayList<>());
    }
}
