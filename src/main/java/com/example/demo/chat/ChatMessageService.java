package com.example.demo.chat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.chat.Dto.ChatMessageDto;
import com.example.demo.chatroom.ChatRoom;
import com.example.demo.chatroom.ChatRoomService;
import com.example.demo.chatroom.Repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRespository repository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final ProfileRepository profileRepository;

    public ChatMessage save(ChatMessageDto chatMessage) {
        ChatRoom chatRoom = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId());

        Optional<Profile> sender = profileRepository.findProfilesById(chatMessage.getSenderId());
            Optional<Profile> recipient = profileRepository.findProfilesById(chatMessage.getRecipientId());
            ChatMessage chat = new ChatMessage();
            chat.setRecipient(recipient.get());
            chat.setSender(sender.get());
            chat.setStatus(MessageStatus.SENT);
            chat.setContent(chatMessage.getContent());
            chat.setTimestamp(new Date());
            chat.setChatRoom(chatRoom);

            repository.save(chat);
        return chat;
        
    }

    public ChatMessage updateStatus(Long chatMessageId, MessageStatus status) {
        Optional<ChatMessage> chatMessage = repository.findById(chatMessageId);
        if (chatMessage.isPresent()) {
            ChatMessage chat = chatMessage.get();
            chat.setStatus(status);
            ChatMessage saved = repository.save(chat);
            return saved;
        }else{
            return null;
        }
    }

    public List<Map<String, Object>> findChatMessages(Long senderId, Long recipientId) {
        
        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(
    senderId, recipientId, recipientId, senderId
);
        return repository.findByChatRoomCustom(chatRoom.get()).orElse(new ArrayList<>());
    }
}
