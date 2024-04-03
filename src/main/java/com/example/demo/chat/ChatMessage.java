package com.example.demo.chat;

import java.util.Date;

import org.springframework.lang.NonNull;

import com.example.demo.chatroom.ChatRoom;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

        private String senderId;

        @NonNull
        private String recipientId;

        private String content;

        private Date timestamp;
}
