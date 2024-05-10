package com.example.demo.chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Components.Helper.Helper;
import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Repositories.UserManagement.AccountManagement.ProfileRepository;
import com.example.demo.chat.Dto.ChatMessageDto;
import com.example.demo.chatroom.ChatRoom;
import com.example.demo.chatroom.ChatRoomService;
import com.example.demo.chatroom.GroupChat;
import com.example.demo.chatroom.Repository.ChatRoomRepository;
import com.example.demo.chatroom.Repository.GroupChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRespository repository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final ProfileRepository profileRepository;
    private final GroupChatRepository groupChatRepository;
    private final GroupChatMessageRepository groupChatMessageRepository;
    private final Helper helper;

    public ChatMessage save(ChatMessageDto chatMessage) {
        ChatRoom chatRoom = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId());

        Optional<Profile> sender = profileRepository.findProfilesById(chatMessage.getSenderId());
            Optional<Profile> recipient = profileRepository.findProfilesById(chatMessage.getRecipientId());
            ChatMessage chat = new ChatMessage();
            chat.setRecipient(recipient.get());
            chat.setSender(sender.get());
            chat.setStatus(MessageStatus.SENT);
            chat.setContent(chatMessage.getContent());
            chat.setTimestamp(LocalDateTime.now());
            chat.setChatRoom(chatRoom);

            repository.save(chat);
        return chat;
        
    }

    public GroupChatMessage groupMsgSave(ChatMessageDto chatMessage) {
        ChatRoom chatRoom = chatRoomService.getGroupChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId());

        Optional<Profile> sender = profileRepository.findProfilesById(chatMessage.getSenderId());
        GroupChat recipient = groupChatRepository.findGroupChatIdById(chatMessage.getRecipientId()).get();
           // Optional<Profile> recipient = profileRepository.findProfilesById(chatMessage.getRecipientId());
            GroupChatMessage chat = new GroupChatMessage();
            chat.setRecipient(recipient);
            chat.setSender(sender.get());
            chat.setStatus(MessageStatus.SENT);
            chat.setContent(chatMessage.getContent());
            chat.setTimestamp(new Date());
            chat.setChatRoom(chatRoom);

            groupChatMessageRepository.save(chat);
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
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(
                senderId, recipientId, recipientId, senderId
        );
        if (chatRoomOptional.isPresent()) {
            ChatRoom chatRoom = chatRoomOptional.get();
            return helper.mapChatTimer(repository.findByChatRoomCustom(chatRoom).get());
        } else {
            // Handle the case where the chat room is not found
            return new ArrayList<>();
        }
    }

    public List<Map<String, Object>> findGroupChatMessages(Long senderId, Long recipientId) {
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findBySenderIdAndGroup(senderId, recipientId);
        if (chatRoomOptional.isPresent()) {
            ChatRoom chatRoom = chatRoomOptional.get();
            return repository.findByChatRoomCustom(chatRoom).orElse(new ArrayList<>());
        } else {
            // Handle the case where the chat room is not found
            return new ArrayList<>();
        }
    }
}
