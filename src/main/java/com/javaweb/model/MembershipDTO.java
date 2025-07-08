package com.javaweb.model;

public class MembershipDTO {
    private Integer membershipId;
    private String type;
    private Double price;
    private String description;

    public MembershipDTO() {}

    public MembershipDTO(Integer membershipId, String type, Double price, String description) {
        this.membershipId = membershipId;
        this.type = type;
        this.price = price;
        this.description = description;
    }

    public Integer getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Integer membershipId) {
        this.membershipId = membershipId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
