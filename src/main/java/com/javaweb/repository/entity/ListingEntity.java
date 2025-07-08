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
    private Integer listingId;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "Listing_status")
    private boolean listingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyID", referencedColumnName = "PropertyID")
    private PropertyEntity property;

    @Column(name = "listing_type")
    private String listingType;



    public String getListingType() {
        return listingType;
    }

    public void setListingType(String listingType) {
        this.listingType = listingType;
    }

    public ListingEntity() {
    }


    public PropertyEntity getProperty() {
        return property;
    }

    public void setProperty(PropertyEntity property) {
        this.property = property;
    }

    public ListingEntity(Integer listingId, String description, boolean listingStatus, PropertyEntity property, String listingType) {
        this.listingId = listingId;
        this.description = description;
        this.listingStatus = listingStatus;
        this.property = property;
        this.listingType = listingType;
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

}
