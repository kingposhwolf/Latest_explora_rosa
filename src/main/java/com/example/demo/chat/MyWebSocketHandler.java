// package com.example.demo.chat;

// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.socket.TextMessage;
// import org.springframework.web.socket.WebSocketSession;
// import org.springframework.web.socket.handler.TextWebSocketHandler;

// @RestController
// public class MyWebSocketHandler extends TextWebSocketHandler{
//     @SuppressWarnings("null")
//    // @MessageMapping("/chat") // Define the path at which clients can access WebSocket functionality
//     protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//         super.handleTextMessage(session, message);

//         // Process the incoming message
//         // For demonstration purposes, let's just log the message
//         System.out.println("Received message from client: " + message.getPayload());

//         // Send ACK back to the client
//         session.sendMessage(new TextMessage("ACK"));
//     }
// }
