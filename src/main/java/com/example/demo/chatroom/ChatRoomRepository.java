package com.example.demo.chatroom;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ChatRoomRepository extends CrudRepository<ChatRoom,Long>{
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
