package com.example.demo.Repositories.Chat;
// package com.example.demo.Repositories;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

// import com.example.demo.Models.Chat.ChatHistory;
// import com.example.demo.Models.UserManagement.User;

// public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long>{
//     Optional<ChatHistory> findBySenderIdAndRecepient(String senderId, User recipient);

//      @Query("SELECT ch.recepient FROM ChatHistory ch WHERE ch.senderId = :senderId")
//     List<User> findRecepientBySenderId(@Param("senderId") String senderId);
// }
