package com.javaweb.repository.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "membership", schema = "swp_dreamhouse")
public class MembershipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membershipID")
    private Integer membershipId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "price", nullable = false)
    private Double price;


    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "num_listings")
    private Integer numListings;

    @Column(name = "num_listings_vip")
    private Integer numListingsVip;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // === Constructors ===
    public MembershipEntity() {}

    public MembershipEntity(Integer membershipId, String type, Double price, String description, Integer numListings, Integer numListingsVip) {
        this.membershipId = membershipId;
        this.type = type;
        this.price = price;
        this.description = description;
        this.numListings = numListings;
        this.numListingsVip = numListingsVip;
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

    // === Getters & Setters ===
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
