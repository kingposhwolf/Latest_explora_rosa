package com.example.demo.chatroom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.chatroom.GroupChatRoom;

public interface GroupChatRoomRepository extends JpaRepository<GroupChatRoom, Long>{
    
}
