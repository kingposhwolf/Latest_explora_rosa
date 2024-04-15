package com.example.demo.chatroom;

import com.example.demo.Models.UserManagement.Profile;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "senderId", nullable = false, foreignKey = @ForeignKey(name = "FK_PROFILE_SENDER_CHATROOM", foreignKeyDefinition = "FOREIGN KEY (sender_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile sender;

    @ManyToOne
    @JoinColumn(name = "recipientId", nullable = false, foreignKey = @ForeignKey(name = "FK_PROFILE_RECIPIENT_CHATROOM", foreignKeyDefinition = "FOREIGN KEY (recipient_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile recipient;
}