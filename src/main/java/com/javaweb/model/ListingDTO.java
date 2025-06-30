package com.javaweb.model;

public class ListingDTO {

    private Long listingId;
    private String description;
    private String listingStatus;
    private Long propertyId;

    // === Constructors ===
    public ListingDTO() {
    }

    public ListingDTO(Long listingId, String description, String listingStatus, Long propertyId) {
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
