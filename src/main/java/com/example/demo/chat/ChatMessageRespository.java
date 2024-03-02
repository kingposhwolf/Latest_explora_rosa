package com.example.demo.chat;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ChatMessageRespository extends CrudRepository<ChatMessage, Long>{
    List<ChatMessage> findByChatId(String chatId);
}
