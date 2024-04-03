package com.example.demo.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.chatroom.ChatRoom;


public interface ChatMessageRespository extends CrudRepository<ChatMessage, Long>{
    Optional<List<ChatMessage>> findByChatRoom(ChatRoom chatRoom);
}
