package com.example.demo.chat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.demo.chatroom.ChatRoom;


public interface ChatMessageRespository extends CrudRepository<ChatMessage, Long>{
    
    @Query("SELECT cm.id as id,cm.sender.id as senderId, cm.content as content, cm.recipient as recipientId, cm.status as status FROM ChatMessage cm WHERE cm.chatRoom = :chatRoom")
    Optional<List<Map<String, Object>>> findByChatRoomCustom(ChatRoom chatRoom);
}
