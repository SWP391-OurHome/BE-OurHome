package com.javaweb.repository.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "property_images")
public class PropertyImageEnitity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "image_url", nullable = false)
    private String imageUrl;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private PropertyEntity property;


    // Constructors
    public PropertyImageEnitity() {}


    public PropertyImageEnitity(String imageUrl, PropertyEntity property) {
        this.imageUrl = imageUrl;
        this.property = property;
    }


    // Getters and Setters
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getImageUrl() {
        return imageUrl;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public PropertyEntity getProperty() {
        return property;
    }


    public void setProperty(PropertyEntity property) {
        this.property = property;
    }


    // Optional: toString (Avoid accessing lazy property directly)
    @Override
    public String toString() {
        return "PropertyImage{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

