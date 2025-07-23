package com.javaweb.controller;


import com.javaweb.model.ChatMessageDTO;
import com.javaweb.model.ConversationDTO;
import com.javaweb.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/chat")
public class ChatRestController {


    @Autowired
    private ChatService chatService;


    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<ConversationDTO>> getConversations(@PathVariable Integer userId) {
        List<ConversationDTO> conversations = chatService.getConversations(userId);
        return ResponseEntity.ok(conversations);
    }


    @GetMapping("/messages/{userId}/{conversationId}")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Integer userId,
                                                            @PathVariable Integer conversationId
    ) {
        try {
            List<ChatMessageDTO> messages = chatService.getMessagesByConversation(userId, conversationId);
            if (messages == null || messages.isEmpty()) {
                return ResponseEntity.ok().body(null); // Trả về null nếu không có tin nhắn
            }
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Xử lý lỗi
        }
    }
}



