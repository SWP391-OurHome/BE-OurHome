package com.javaweb.service;


import com.javaweb.model.ChatMessageDTO;
import com.javaweb.model.ConversationDTO;


import java.util.List;


public interface ChatService {
    ChatMessageDTO saveMessage(ChatMessageDTO messageDTO);
    public List<ChatMessageDTO> getMessagesBetweenUsers(Integer userId, Integer otherUserId);
    List<ConversationDTO> getConversations(Integer userId);
    public List<ChatMessageDTO> getMessagesByConversation(Integer userId, Integer conversationId);
}



