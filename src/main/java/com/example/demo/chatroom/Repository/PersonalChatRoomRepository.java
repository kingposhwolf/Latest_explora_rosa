package com.example.demo.chatroom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.chatroom.PersonalChatRoom;

@Repository
public interface PersonalChatRoomRepository extends JpaRepository<PersonalChatRoom, Long>{
    
}
