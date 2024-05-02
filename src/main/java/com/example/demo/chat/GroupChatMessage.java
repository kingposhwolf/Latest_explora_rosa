package com.example.demo.chat;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.chatroom.ChatRoom;
import com.example.demo.chatroom.GroupChat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "chatRoomId")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "senderId", nullable = false, foreignKey = @ForeignKey(name = "FK_PROFILE_SENDER_GROUP_CHAT", foreignKeyDefinition = "FOREIGN KEY (sender_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile sender;

    @ManyToOne
    @JoinColumn(name = "recipientId", nullable = false, foreignKey = @ForeignKey(name = "FK_PROFILE_RECIPIENT_GROUP_CHAT", foreignKeyDefinition = "FOREIGN KEY (recipient_id) REFERENCES group_chat(id) ON DELETE CASCADE"))
    private GroupChat recipient;

    @NotNull
    private String content;

    private Date timestamp;

    private MessageStatus status;
}
