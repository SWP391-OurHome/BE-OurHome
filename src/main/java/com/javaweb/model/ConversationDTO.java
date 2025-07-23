package com.javaweb.model;


import java.util.List;


public class ConversationDTO {
    private Integer id;
    private String sender; // Username (firstName + lastName)
    private String lastMessage;
    private String time; // Formatted timestamp
    private String avatar; // From UserEntity.imgPath
    private boolean isVoteSection;
    private List<String> messages;


    private Integer userId1;
    private Integer userId2;


    public Integer getUserId1() {
        return userId1;
    }


    public void setUserId1(Integer userId1) {
        this.userId1 = userId1;
    }


    public Integer getUserId2() {
        return userId2;
    }


    public void setUserId2(Integer userId2) {
        this.userId2 = userId2;
    }


    public List<String> getMessages() {
        return messages;
    }


    public void setMessages(List<String> messages) {
        this.messages = messages;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getLastMessage() {
        return lastMessage;
    }


    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }


    public String getSender() {
        return sender;
    }


    public void setSender(String sender) {
        this.sender = sender;
    }


    public String getTime() {
        return time;
    }


    public void setTime(String time) {
        this.time = time;
    }


    public String getAvatar() {
        return avatar;
    }


    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public boolean isVoteSection() {
        return isVoteSection;
    }


    public void setVoteSection(boolean voteSection) {
        isVoteSection = voteSection;
    }
}



