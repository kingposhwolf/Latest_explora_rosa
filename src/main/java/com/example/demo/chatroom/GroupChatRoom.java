package com.example.demo.chatroom;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("GROUP")
public class GroupChatRoom extends ChatRoom{
    @ManyToOne
    @JoinColumn(name = "recipientId", nullable = false, foreignKey = @ForeignKey(name = "FK_PROFILE_RECIPIENT_GROUPCHATROOM", foreignKeyDefinition = "FOREIGN KEY (recipient_id) REFERENCES group_chat(id) ON DELETE CASCADE"))
    private GroupChat groupRecipient;
}
