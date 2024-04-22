package com.example.demo.chatroom;

import com.example.demo.Models.UserManagement.Profile;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("ONE_TO_ONE")
public class PersonalChatRoom extends ChatRoom{
    @ManyToOne
    @JoinColumn(name = "recipientId", nullable = false, foreignKey = @ForeignKey(name = "FK_PROFILE_RECIPIENT_CHATROOM", foreignKeyDefinition = "FOREIGN KEY (recipient_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile recipient;
}
