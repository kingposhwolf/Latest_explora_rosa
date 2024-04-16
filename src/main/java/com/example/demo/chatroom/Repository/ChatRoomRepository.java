package com.example.demo.chatroom.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.chatroom.ChatRoom;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long>{

    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.sender.id = :senderId AND cr.recipient.id = :recipientId) OR (cr.sender.id = :sender2Id AND cr.recipient.id = :recipient2Id) OR (cr.sender.id = :senderId AND cr.groupRecipient.id = :recipientId)")
Optional<ChatRoom> findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(
    @Param("senderId") Long senderId,
    @Param("recipientId") Long recipientId,
    @Param("sender2Id") Long sender2Id,
    @Param("recipient2Id") Long recipient2Id);

    @Query("SELECT cr.sender.id as senderProfileId, cr.sender.user.name as senderName , cr.sender.user.username as senderUsername, cr.recipient.id as recipientProfileId, cr.recipient.user.name as recipientName , cr.recipient.user.username as recipientUsername  FROM ChatRoom cr WHERE cr.sender.id = :senderId OR cr.recipient.id = :senderId")
    Optional<List<Map<String, Object>>> findBychatRoomContainsUser(@Param("senderId") Long senderId);
}
