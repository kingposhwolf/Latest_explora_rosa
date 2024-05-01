package com.example.demo.chat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.chatroom.ChatRoom;


public interface ChatMessageRespository extends CrudRepository<ChatMessage, Long>{
    
    @Query("SELECT cm.id as id,cm.sender.id as senderId, cm.content as content, cm.recipient.id as recipientId, cm.status as status, cm.timestamp as timestamp FROM ChatMessage cm WHERE cm.chatRoom = :chatRoom")
    Optional<List<Map<String, Object>>> findByChatRoomCustom(ChatRoom chatRoom);

    @Query("SELECT COUNT(cm.id) FROM ChatMessage cm WHERE cm.recipient.id = :recipientId AND cm.chatRoom.id = :chatRoomId AND cm.status = com.example.demo.chat.MessageStatus.DELIVERED")
    Long unreadMessages(@Param("recipientId") Long recipientId, @Param("chatRoomId") Long chatRoomId);

    @Query("SELECT cm.id as id, cm.sender.id as senderId, cm.content as content, cm.recipient.id as recipientId, cm.status as status, cm.timestamp as timestamp FROM ChatMessage cm " +
    "WHERE cm.chatRoom.id = :chatRoomId " +
    "AND cm.timestamp = (SELECT MAX(cm2.timestamp) FROM ChatMessage cm2 WHERE cm2.chatRoom.id = :chatRoomId)")
    Map<String, Object> lastMessage(@Param("chatRoomId")Long chatRoomId);


    @Modifying
    @Query("UPDATE ChatMessage cm SET cm.status = com.example.demo.chat.MessageStatus.DELIVERED WHERE cm.recipient.id = :recipientId AND cm.status = com.example.demo.chat.MessageStatus.SENT")
    int updateMessageStatusToDelivered(@Param("recipientId") Long recipientId);

}
