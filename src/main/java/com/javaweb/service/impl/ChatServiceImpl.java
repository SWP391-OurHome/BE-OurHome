package com.javaweb.service.impl;


import com.javaweb.model.ChatMessageDTO;
import com.javaweb.model.ConversationDTO;
import com.javaweb.repository.entity.ChatEntity;
import com.javaweb.repository.entity.ConversationEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.ChatRepositoryImpl;
import com.javaweb.repository.impl.ConversationRepositoryImpl;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ChatServiceImpl implements ChatService {


    @Autowired
    private ChatRepositoryImpl chatRepository;


    @Autowired
    private UserRepositoryImpl userRepository;


    @Autowired
    private ConversationRepositoryImpl conversationRepository;


    @Override
    public ChatMessageDTO saveMessage(ChatMessageDTO messageDTO) {
        UserEntity sender = userRepository.findById(messageDTO.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        UserEntity receiver = userRepository.findById(messageDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));


        // Tìm hoặc tạo Conversation
        ConversationEntity conversation = conversationRepository.findByUsers(sender.getUserId(), receiver.getUserId())
                .orElseGet(() -> {
                    ConversationEntity newConversation = new ConversationEntity();
                    newConversation.setUser1(sender.getUserId() < receiver.getUserId() ? sender : receiver);
                    newConversation.setUser2(sender.getUserId() < receiver.getUserId() ? receiver : sender);
                    return conversationRepository.save(newConversation);
                });


        ChatEntity entity = new ChatEntity();
        entity.setSender(sender);
        entity.setReceiver(receiver);
        entity.setConversation(conversation);
        entity.setContent(messageDTO.getContent());
        entity.setTimestamp(messageDTO.getTimestamp());
        entity.setStatus(messageDTO.getStatus());
        ChatEntity saved = chatRepository.save(entity);


        return convertToDTO(saved);
    }


    @Override
    public List<ChatMessageDTO> getMessagesBetweenUsers(Integer userId, Integer otherUserId) {
        return chatRepository.findMessagesBetweenUsers(userId, otherUserId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<ConversationDTO> getConversations(Integer userId) {
        System.out.println("🔍 Getting conversations for userId: " + userId);


        List<ConversationEntity> conversations = chatRepository.findConversationsByUserId(userId);
        System.out.println("✅ Found " + conversations.size() + " conversations.");


        return conversations.stream().map(conversation -> {
            System.out.println("\n➡️ Mapping conversation ID: " + conversation.getId());


            ConversationDTO dto = new ConversationDTO();
            dto.setId(conversation.getId());


            UserEntity user1 = conversation.getUser1();
            UserEntity user2 = conversation.getUser2();


            UserEntity otherUser = user1.getUserId().equals(userId) ? user2 : user1;
            dto.setSender(otherUser.getFirstName() + " " + otherUser.getLastName());
            dto.setAvatar(otherUser.getImgPath());


            List<ChatEntity> messages = chatRepository.findMessagesBetweenUsers(userId, otherUser.getUserId());
            System.out.println("   💬 Found " + messages.size() + " messages between userId " + userId + " and " + otherUser.getUserId());


            if (!messages.isEmpty()) {
                messages.sort(Comparator.comparing(ChatEntity::getTimestamp)); // Đảm bảo thứ tự thời gian tăng dần
                ChatEntity lastMessage = messages.get(messages.size() - 1);
                dto.setLastMessage(lastMessage.getContent());
                dto.setTime(lastMessage.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm")));


                // Thêm tất cả nội dung tin nhắn vào dto
                List<String> messageContents = messages.stream()
                        .map(ChatEntity::getContent)
                        .collect(Collectors.toList());
                dto.setMessages(messageContents);
            } else {
                dto.setLastMessage("");
                dto.setTime("");
                dto.setMessages(List.of()); // danh sách rỗng
            }


            dto.setVoteSection(false);
            dto.setUserId1(conversation.getUser1().getUserId());
            dto.setUserId2(conversation.getUser2().getUserId());
            return dto;
        }).collect(Collectors.toList());
    }






    public List<ChatMessageDTO> getMessagesByConversation(Integer userId, Integer conversationId) {
        // Tìm các conversation của userId
        List<ConversationEntity> conversations = chatRepository.findConversationsByUserId(userId);


        // Tìm conversation cụ thể với conversationId
        Optional<ConversationEntity> conversation = conversations.stream()
                .filter(conv -> conv.getId().equals(conversationId))
                .findFirst();


        if (conversation.isEmpty()) {
            return Collections.emptyList(); // Trả về danh sách rỗng nếu không tìm thấy conversation
        }


        // Lấy otherUserId từ conversation
        ConversationEntity convEntity = conversation.get();
        Integer otherUserId = convEntity.getUser1().getUserId().equals(userId)
                ? convEntity.getUser2().getUserId()
                : convEntity.getUser1().getUserId();


        // Truy vấn các tin nhắn với conversationId, userId và otherUserId
        return chatRepository.findByConversationIdAndUsers(conversationId, userId, otherUserId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private ChatMessageDTO convertToDTO(ChatEntity entity) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(entity.getId());
        dto.setSenderId(entity.getSender().getUserId());
        dto.setSenderUsername(entity.getSender().getFirstName() + " " + entity.getSender().getLastName());
        dto.setSenderAvatar(entity.getSender().getImgPath());
        dto.setReceiverId(entity.getReceiver().getUserId());
        dto.setReceiverUsername(entity.getReceiver().getFirstName() + " " + entity.getReceiver().getLastName());
        dto.setContent(entity.getContent());
        dto.setTimestamp(entity.getTimestamp());
        dto.setStatus(entity.getStatus());
        dto.setConversationId(entity.getConversation().getId());
        return dto;
    }
}



