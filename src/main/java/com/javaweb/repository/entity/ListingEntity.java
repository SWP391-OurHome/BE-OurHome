package com.javaweb.repository.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Listing")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ListingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Listing_ID")
    private Long listingId;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "Listing_status")
    private String listingStatus;

    @Column(name = "PropertyID")
    private Long propertyId;

    // === Constructors ===

    public ListingEntity() {
    }

    public ListingEntity(Long listingId, String description, String listingStatus, Long propertyId) {
        this.listingId = listingId;
        this.description = description;
        this.listingStatus = listingStatus;
        this.propertyId = propertyId;
    }

    // === Getters & Setters ===

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(String listingStatus) {
        this.listingStatus = listingStatus;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }
}
