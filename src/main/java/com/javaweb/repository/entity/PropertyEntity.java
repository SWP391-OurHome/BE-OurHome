package com.javaweb.repository.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List; //
import java.util.ArrayList;


@Entity
@Table(name = "Property")

public class PropertyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PropertyID")
    private Integer  id;

    @Column(name = "Address_Line1")
    private String addressLine1;

    @Column(name = "Address_Line2")
    private String addressLine2;

    @Column(name = "Region")
    private String region;

    @Column(name = "City")
    private String city;

    @Column(name = "area")
    private Double area;

    @Column(name = "interior")
    private Double interior;

    @Column(name = "PropertyType")
    private String propertyType;

    @Column(name = "Num_Bedroom")
    private Integer numBedroom;

    @Column(name = "Num_Compares")
    private Integer numCompares;

    @Column(name = "Num_Bathroom")
    private Integer numBathroom;

    @Column(name = "Floor")
    private Integer floor;

    @Column(name = "PrivatePool")
    private Boolean privatePool;

    @Column(name = "LandType")
    private String landType;


    @Column(name = "legal_status")
    private String legalStatus;

    @Column(name = "ImgURL")
    private String imgURL;

    @Column(name = "Purpose")
    private String purpose;

    @Column(name = "price") // Thêm cột Price
    private String  price;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PropertyImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "property-listings")
    private List<ListingEntity> listings = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "UserID")
    private UserEntity user;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getInterior() {
        return interior;
    }

    public void setInterior(Double interior) {
        this.interior = interior;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Integer getNumBedroom() {
        return numBedroom;
    }

    public void setNumBedroom(Integer numBedroom) {
        this.numBedroom = numBedroom;
    }
    public Integer getNumCompares() {
        return numCompares;
    }

    public void setNumCompares(Integer numCompares) {
        this.numCompares = numCompares;
    }

    public Integer getNumBathroom() {
        return numBathroom;
    }

    public void setNumBathroom(Integer numBathroom) {
        this.numBathroom = numBathroom;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Boolean getPrivatePool() {
        return privatePool;
    }

    public void setPrivatePool(Boolean privatePool) {
        this.privatePool = privatePool;
    }

    public String getLandType() {
        return landType;
    }

    public void setLandType(String landType) {
        this.landType = landType;
    }

    public String getLegalStatus() {
        return legalStatus;
    }

    public void setLegalStatus(String legalStatus) {
        this.legalStatus = legalStatus;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public List<PropertyImage> getImages() {
        return images;
    }

    public void setImages(List<PropertyImage> images) {
        this.images = images;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<ListingEntity> getListings() {
        return listings;
    }

    public void setListings(List<ListingEntity> listings) {
        this.listings = listings;
    }
}