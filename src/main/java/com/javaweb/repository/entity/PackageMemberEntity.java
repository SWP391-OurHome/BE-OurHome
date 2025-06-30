package com.javaweb.repository.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PackageMember")
@IdClass(PackageMemberId.class)
public class PackageMemberEntity {

    @Id
    @Column(name = "UserID")
    private Integer userId;

    @Id
    @Column(name = "MembershipID")
    private Integer membershipId;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Column(name = "Status")
    private String status;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getMembershipId() { return membershipId; }
    public void setMembershipId(Integer membershipId) { this.membershipId = membershipId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

