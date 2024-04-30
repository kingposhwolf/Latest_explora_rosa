package com.example.demo.chatroom.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.chatroom.PersonalChatRoom;

@Repository
public interface PersonalChatRoomRepository extends JpaRepository<PersonalChatRoom, Long>{
    @Query("SELECT cr.id as chatRoomId, cr.sender.id as senderProfileId, cr.sender.verificationStatus as senderverificationStatus, cr.sender.profilePicture as senderProfilePicture, cr.sender.user.name as senderName , cr.sender.user.username as senderUsername, cr.recipient.id as recipientProfileId, cr.recipient.verificationStatus as recipientVerificationStatus, cr.recipient.user.name as recipientName , cr.recipient.user.username as recipientUsername, cr.recipient.profilePicture as recipientProfilePicture  FROM PersonalChatRoom cr WHERE cr.sender.id = :senderId OR cr.recipient.id = :senderId")
    Optional<List<Map<String, Object>>> findBychatRoomContainsUser(@Param("senderId") Long senderId);
}
