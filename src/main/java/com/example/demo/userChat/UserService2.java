package com.example.demo.userChat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Components.Helper.Helper;
import com.example.demo.InputDto.ChatUser;
import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Models.UserManagement.Management.Status;
import com.example.demo.OutputDto.ConversationHistory;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.chat.ChatMessageRespository;
import com.example.demo.chatroom.Repository.PersonalChatRoomRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService2 {
    private final ProfileRepository profileRepository;
    private final PersonalChatRoomRepository personalChatRoomRepository;
    private final ChatMessageRespository chatMessageRepository;
    private final Helper helper;

    @Transactional
    public void saveUser(ChatUser user) {
        Optional<Profile> profileOpt = profileRepository.findProfilesById(user.getUsername());
        if(profileOpt.isPresent()){
            Profile profile = profileOpt.get();
            profile.setStatus(Status.ONLINE);
            profileRepository.save(profile);

            //update personal messages that delivered to him form sent to delivered status
            chatMessageRepository.updateMessageStatusToDelivered(profile.getId());
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
        return profileRepository.findByStatus(Status.ONLINE).get();
    }

    public List<ConversationHistory> chatList(Long profileId){
        Optional<List<Map<String, Object>>> chatListOptional = personalChatRoomRepository.findBychatRoomContainsUser(profileId);
        
        if(chatListOptional.isPresent()){
            List<ConversationHistory> conversationList = new ArrayList<>();
            
            List<Map<String, Object>> chatList = chatListOptional.get();
            for (Map<String, Object> chat : chatList) {
                ConversationHistory conversation = new ConversationHistory();
                String senderProfileId = Objects.toString(chat.get("senderProfileId"), null);
                if (senderProfileId != null && senderProfileId.equals(String.valueOf(profileId))) {
                    conversation.setProfileId(Long.parseLong(Objects.toString(chat.get("recipientProfileId"), null)));
                    conversation.setName(Objects.toString(chat.get("recipientName"), null));
                    conversation.setUsername(Objects.toString(chat.get("recipientUsername"), null));
                    conversation.setVerificationStatus(Objects.toString(chat.get("recipientVerificationStatus"), null));
                    conversation.setProfilePicture(Objects.toString(chat.get("recipientProfilePicture"), null));
                } else {
                    conversation.setProfileId(Long.parseLong(Objects.toString(chat.get("senderProfileId"), null)));
                    conversation.setName(Objects.toString(chat.get("senderName"), null));
                    conversation.setUsername(Objects.toString(chat.get("senderUsername"), null));
                    conversation.setVerificationStatus(Objects.toString(chat.get("senderVerificationStatus"), null));
                    conversation.setProfilePicture(Objects.toString(chat.get("senderProfilePicture"), null));
                }

                conversation.setUnreadMessages(chatMessageRepository.unreadMessages(profileId, Long.parseLong(Objects.toString(chat.get("chatRoomId"), null))));
                conversation.setLastMessage(helper.mapSingleTimer(chatMessageRepository.lastMessage(Long.parseLong(Objects.toString(chat.get("chatRoomId"), null)))));
                conversationList.add(conversation);
            }
            return conversationList;
        }
        return new ArrayList<>();
    }
}
