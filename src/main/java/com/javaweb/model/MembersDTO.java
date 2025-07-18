package com.javaweb.model;

import java.time.LocalDate;

public class MembersDTO {

    private Integer id;
    private Integer userId;
    private Integer membershipId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean status;

    // Constructors
    public MembersDTO() {}

    public MembersDTO(Integer id, Integer userId, Integer membershipId, LocalDate startDate, LocalDate endDate, Boolean status) {
        this.id = id;
        this.userId = userId;
        this.membershipId = membershipId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Integer membershipId) {
        this.membershipId = membershipId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}