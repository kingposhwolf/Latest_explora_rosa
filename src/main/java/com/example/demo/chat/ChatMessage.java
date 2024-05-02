package com.example.demo.chat;

import java.time.LocalDateTime;
import java.util.Date;

import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.chatroom.ChatRoom;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "chatRoomId")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "senderId", nullable = false, foreignKey = @ForeignKey(name = "FK_PROFILE_SENDER_CHAT", foreignKeyDefinition = "FOREIGN KEY (recipient_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile sender;

    @ManyToOne
    @JoinColumn(name = "recipientId", nullable = false, foreignKey = @ForeignKey(name = "FK_PROFILE_RECIPIENT_CHAT", foreignKeyDefinition = "FOREIGN KEY (recipient_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile recipient;

    @NotNull
    private String content;

    private LocalDateTime timestamp;

    private MessageStatus status;
}
