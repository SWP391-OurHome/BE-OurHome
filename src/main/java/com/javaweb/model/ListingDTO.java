package com.javaweb.model;

public class ListingDTO {

    private Integer listingId;
    private String description;
    private String listingStatus;
    private Integer propertyId;

    // === Constructors ===
    public ListingDTO() {
    }

    public ListingDTO(Integer listingId, String description, String listingStatus, Integer propertyId) {
        this.listingId = listingId;
        this.description = description;
        this.listingStatus = listingStatus;
        this.propertyId = propertyId;
    }

    // === Getters & Setters ===
    public Integer getListingId() {
        return listingId;
    }

    public void setListingId(Integer listingId) {
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

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

}
