package com.example.demo.chatroom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.chatroom.GroupChat;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long>{
    
}
