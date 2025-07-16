package com.javaweb.model;

public class TopPropertyDTO {
    private Integer id;
    private String name;
    private String price;
    private int contacts;
    private String image;

    public TopPropertyDTO(Integer id, String name, String price, int contacts, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.contacts = contacts;
        this.image = image;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getContacts() {
        return contacts;
    }

    public void setContacts(int contacts) {
        this.contacts = contacts;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}