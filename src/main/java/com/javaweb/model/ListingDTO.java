package com.javaweb.model;

public class ListingDTO {

    private Integer listingId;
    private String description;
    private boolean listingStatus;
    private Integer propertyId;
    private String lisitngType;
    // === Constructors ===
    public ListingDTO() {
    }

    public ListingDTO(Integer listingId, String description, boolean listingStatus, Integer propertyId,String lisitngType) {
        this.listingId = listingId;
        this.description = description;
        this.listingStatus = listingStatus;
        this.propertyId = propertyId;
        this.lisitngType = lisitngType;
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

    public boolean getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(boolean listingStatus) {
        this.listingStatus = listingStatus;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public boolean isListingStatus() {
        return listingStatus;
    }

    public String getLisitngType() {
        return lisitngType;
    }

    public void setLisitngType(String lisitngType) {
        this.lisitngType = lisitngType;
    }
}
