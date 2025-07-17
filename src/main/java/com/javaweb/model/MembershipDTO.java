package com.javaweb.model;

public class MembershipDTO {
    private Integer membershipId;
    private String type;
    private Double price;
    private String description;
    private Integer numListings;
    private Integer numListingsVip;
    private Boolean isActive;

    public MembershipDTO() {
    }

    public MembershipDTO(Integer membershipId, String type, Double price, String description, Integer numListings, Integer numListingsVip, Boolean isActive) {
        this.membershipId = membershipId;
        this.type = type;
        this.price = price;
        this.description = description;
        this.numListings = numListings;
        this.numListingsVip = numListingsVip;
        this.isActive = isActive;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getNumListings() {
        return numListings;
    }

    public void setNumListings(Integer numListings) {
        this.numListings = numListings;
    }

    public Integer getNumListingsVip() {
        return numListingsVip;
    }

    public void setNumListingsVip(Integer numListingsVip) {
        this.numListingsVip = numListingsVip;
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
