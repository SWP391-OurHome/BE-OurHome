package com.javaweb.repository.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "FavouriteListing") // Giữ nguyên tên bảng là "FavouriteListing"
public class FavouriteListingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FavouriteID")
    private Integer favouriteId;


    @Column(name = "UserID", nullable = false)
    private Integer userId;


    @Column(name = "ListingID", nullable = false)
    private Integer listingId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private UserEntity user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ListingID", insertable = false, updatable = false)
    private ListingEntity listing;


    // Constructors
    public FavouriteListingEntity() {}


    public FavouriteListingEntity(Integer userId, Integer listingId) {
        this.userId = userId;
        this.listingId = listingId;
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


    public Integer getListingId() {
        return listingId;
    }


    public void setListingId(Integer listingId) {
        this.listingId = listingId;
    }


    public UserEntity getUser() {
        return user;
    }


    public void setUser(UserEntity user) {
        this.user = user;
    }


    public ListingEntity getListing() {
        return listing;
    }


    public void setListing(ListingEntity listing) {
        this.listing = listing;
    }
}

