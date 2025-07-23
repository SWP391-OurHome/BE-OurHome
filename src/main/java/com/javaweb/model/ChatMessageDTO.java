package com.javaweb.model;


import java.time.LocalDateTime;


public class ChatMessageDTO {
    private Integer id;
    private Integer senderId;
    private String senderUsername; // Derived from firstName + lastName
    private String senderAvatar; // From UserEntity.imgPath
    private Integer receiverId;
    private String receiverUsername; // Derived from firstName + lastName
    private String content;
    private LocalDateTime timestamp;
    private String status;
    private Integer conversationId;


    public Integer getConversationId() {
        return conversationId;
    }


    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public Integer getSenderId() {
        return senderId;
    }


    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getSenderUsername() {
        return senderUsername;
    }


    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }


    public String getSenderAvatar() {
        return senderAvatar;
    }


    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }


    public String getReceiverUsername() {
        return receiverUsername;
    }


    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }


    public Integer getReceiverId() {
        return receiverId;
    }


    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}



