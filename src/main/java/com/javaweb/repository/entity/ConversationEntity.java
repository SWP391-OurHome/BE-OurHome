package com.javaweb.repository.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "conversations")
public class ConversationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private UserEntity user1;


    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private UserEntity user2;


    // Getters and setters
    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public UserEntity getUser1() {
        return user1;
    }


    public void setUser1(UserEntity user1) {
        this.user1 = user1;
    }


    public UserEntity getUser2() {
        return user2;
    }


    public void setUser2(UserEntity user2) {
        this.user2 = user2;
    }
}



