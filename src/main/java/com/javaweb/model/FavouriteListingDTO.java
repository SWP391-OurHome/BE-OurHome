package com.javaweb.model;

public class FavouriteListingDTO {
    private Integer favouriteId;
    private Integer userId;
    private Integer propertyId;
    private PropertyDTO property; // Thêm PropertyDTO

    // Constructors
    public FavouriteListingDTO() {}

    public FavouriteListingDTO(Integer userId, Integer favouriteId, Integer propertyId, PropertyDTO property) {
        this.userId = userId;
        this.favouriteId = favouriteId;
        this.propertyId = propertyId;
        this.property = property;
    }

    // Getters and Setters
    public Integer getFavouriteId() {
        return favouriteId;
    }

    public void setFavouriteId(Integer favouriteId) {
        this.favouriteId = favouriteId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public PropertyDTO getProperty() {
        return property;
    }

    public void setProperty(PropertyDTO property) {
        this.property = property;
    }
}
