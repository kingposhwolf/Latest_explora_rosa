package com.example.demo.chatroom;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.UserManagement.Profile;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@SQLDelete(sql = "UPDATE chat_room SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "CHATROOM_TYPE", discriminatorType = DiscriminatorType.STRING)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "senderId", nullable = false, foreignKey = @ForeignKey(name = "FK_PROFILE_SENDER_CHATROOM", foreignKeyDefinition = "FOREIGN KEY (sender_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile sender;

    @Builder.Default
    private boolean deleted = Boolean.FALSE;
}