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
    private String listingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyID", referencedColumnName = "PropertyID")
    private PropertyEntity property;


    public ListingEntity() {
    }


    public PropertyEntity getProperty() {
        return property;
    }

    public void setProperty(PropertyEntity property) {
        this.property = property;
    }

    public ListingEntity(Integer listingId, String description, String listingStatus, PropertyEntity property) {
        this.listingId = listingId;
        this.description = description;
        this.listingStatus = listingStatus;
        this.property = property;
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

}
