package com.javaweb.repository.entity;

import java.io.Serializable;
import java.util.Objects;

public class PackageMemberId implements Serializable {

    private Integer userId;
    private Integer membershipId;

    // Default constructor
    public PackageMemberId() {
    }

    // Constructor with fields
    public PackageMemberId(Integer userId, Integer membershipId) {
        this.userId = userId;
        this.membershipId = membershipId;
    }

    // Getters and setters
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

    // Implement equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageMemberId that = (PackageMemberId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(membershipId, that.membershipId);
    }

    // Implement hashCode
    @Override
    public int hashCode() {
        return Objects.hash(userId, membershipId);
    }
}