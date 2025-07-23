package com.javaweb.controller;


import com.javaweb.model.ChatMessageDTO;
import com.javaweb.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;


@Controller
public class ChatWebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketController.class);


    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Autowired
    private ChatService chatService;


    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDTO messageDTO) {
        logger.info("📥 [WS] Received message from client: {}", messageDTO);


        messageDTO.setTimestamp(LocalDateTime.now());
        messageDTO.setStatus("SENT");
        ChatMessageDTO savedMessage = chatService.saveMessage(messageDTO);


        Integer conversationId = savedMessage.getConversationId();


        if (conversationId != null) {
            logger.info("📤 [WS] Broadcasting to /topic/conversation/{}", conversationId);
            messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, savedMessage);
        } else {
            logger.warn("⚠️ No conversationId! Broadcasting to sender and receiver topics: {}, {}",
                    messageDTO.getSenderId(), messageDTO.getReceiverId());


            messagingTemplate.convertAndSend("/topic/" + messageDTO.getSenderId(), savedMessage);
            messagingTemplate.convertAndSend("/topic/" + messageDTO.getReceiverId(), savedMessage);
        }
    }
}

