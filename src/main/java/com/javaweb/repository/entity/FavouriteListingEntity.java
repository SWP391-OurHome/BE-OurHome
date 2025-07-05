package com.javaweb.repository.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "favouritelisting")
public class FavouriteListingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FavouriteID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserID", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PropertyID", nullable = false)
    private PropertyEntity property;

    // Constructors
    public FavouriteListingEntity() {}

    public FavouriteListingEntity(UserEntity user, PropertyEntity property) {
        this.user = user;
        this.property = property;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PropertyEntity getProperty() {
        return property;
    }

    public void setProperty(PropertyEntity property) {
        this.property = property;
    }
}

